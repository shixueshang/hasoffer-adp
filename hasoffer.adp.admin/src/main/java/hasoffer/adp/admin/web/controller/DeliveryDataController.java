package hasoffer.adp.admin.web.controller;

import hasoffer.adp.admin.web.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.TimeUtils;
import hasoffer.adp.base.utils.mail.MailSenderInfo;
import hasoffer.adp.base.utils.mail.SimpleMailSender;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.base.utils.page.PageHelper;
import hasoffer.adp.core.models.po.AccessLog;
import hasoffer.adp.core.models.po.AccessLogDetail;
import hasoffer.adp.core.models.vo.AccessLogDetailVo;
import hasoffer.adp.core.service.AccessLogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2017/1/10 11:28
 */
@Controller
public class DeliveryDataController extends BaseController {

    private static final String DATE_PATTERN = "dd/MMM/yyyy:HH:mm:ss";

    @Resource
    RootConfiguration rootConfiguration;

    @Resource
    AccessLogService accessLogService;

    @Scheduled(cron = "0 0 2 * * ? ")
    public void reqCount() {

        String requestPath = rootConfiguration.getRequestPath();
        String callbackPath = rootConfiguration.getCallbackPath();
        String adimgPath = rootConfiguration.getAdimgPath();
        String adclickPath = rootConfiguration.getAdclickPath();

        Map<String, Integer> reqs = this.count(new File(requestPath), false, null);
        Map<String, Integer> clicks = this.count(new File(adclickPath), false, null);
        Map<String, Integer> imgs = this.count(new File(adimgPath), false, null);
        Map<String, Integer> pvCall = this.count(new File(callbackPath), true, "/pv");
        Map<String, Integer> pvClick = this.count(new File(callbackPath), true, "/click");

        AccessLog log = new AccessLog();
        log.setDate(new Date(hasoffer.base.utils.TimeUtils.yesterday()));
        log.setRequests(reqs.get("count"));
        log.setPvCallback(pvCall.get("count"));
        log.setPvClicks(pvClick.get("count"));
        log.setImgRequests(imgs.get("count"));
        log.setClicks(clicks.get("count"));

        System.out.println("start insert log......");
        accessLogService.insert(log);

        Map<String, Map<String, Integer>> result = new HashMap<>();
        result = this.mapCombine(clicks, result, Constants.LOG_TYPE.CLICKS);
        result = this.mapCombine(imgs, result, Constants.LOG_TYPE.IMG_REQUESTS);
        result = this.mapCombine(pvCall, result, Constants.LOG_TYPE.PV_CALLBACK);
        result = this.mapCombine(pvClick, result, Constants.LOG_TYPE.PV_CLICKS);

        for (Map.Entry entry : result.entrySet()) {
            String mid = entry.getKey().toString();
            Map<String, Integer> cmap = (Map) entry.getValue();
            AccessLogDetail logDetail = new AccessLogDetail();
            logDetail.setDate(new Date(hasoffer.base.utils.TimeUtils.yesterday()));
            logDetail.setMid(Long.parseLong(mid));
            logDetail.setPvCallback(cmap.get(Constants.LOG_TYPE.PV_CALLBACK) == null ? 0 : cmap.get(Constants.LOG_TYPE.PV_CALLBACK));
            logDetail.setPvClicks(cmap.get(Constants.LOG_TYPE.PV_CLICKS) == null ? 0 : cmap.get(Constants.LOG_TYPE.PV_CLICKS));
            logDetail.setImgRequests(cmap.get(Constants.LOG_TYPE.IMG_REQUESTS) == null ? 0 : cmap.get(Constants.LOG_TYPE.IMG_REQUESTS));
            logDetail.setClicks(cmap.get(Constants.LOG_TYPE.CLICKS) == null ? 0 : cmap.get(Constants.LOG_TYPE.CLICKS));
            accessLogService.insertLogDetail(logDetail);
        }


        //发送邮件
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(rootConfiguration.getMailHost());
        mailInfo.setMailServerPort(rootConfiguration.getMailPort());
        mailInfo.setValidate(true);
        mailInfo.setUserName(rootConfiguration.getMailUsername());
        mailInfo.setPassword(rootConfiguration.getMailPassword());
        mailInfo.setFromAddress(rootConfiguration.getMailFromaddress());
        mailInfo.setToAddress(rootConfiguration.getMailToaddress());
        mailInfo.setSubject("日志统计");
        String content = "请求数   pv回调  pv点击  pv点击率  图片请求  点击数  点击率\n";
        DecimalFormat df = new DecimalFormat("#.###");
        double rate1 = pvClick.get("count") / pvCall.get("count") * 100;
        double rate2 = clicks.get("count") / imgs.get("count") * 100;
        content += reqs.get("count") + "  " + pvCall.get("count") + "        " + pvClick.get("count") + "         " + df.format(rate1) + "%        " + imgs.get("count") + "\t        " + clicks.get("count") + "\t     " + df.format(rate2) + "%";
        mailInfo.setContent(content);

        SimpleMailSender sms = new SimpleMailSender();
        try {
            sms.sendTextMail(mailInfo);
            System.out.println("send mail success....");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/data/list")
    public String countPage() {
        return "redirect:/data/find";
    }

    @RequestMapping(value = "/data/find", method = RequestMethod.GET)
    public ModelAndView dataCount(@RequestParam(value = "dateTimeStart", required = false) Date dateTimeStart, @RequestParam(value = "dateTimeEnd", required = false) Date dateTimeEnd) {
        ModelAndView mav = new ModelAndView("/setting/dataCount");
        if (dateTimeStart == null || dateTimeEnd == null) {
            dateTimeStart = TimeUtils.getFirstDayOfMonth();
            dateTimeEnd = TimeUtils.now();
        }
        Page<AccessLog> pageResult = accessLogService.findPage(page, size, dateTimeStart, dateTimeEnd);
        mav.addObject("page", PageHelper.getPageModel(request, pageResult));
        mav.addObject("logs", pageResult.getItems());
        mav.addObject("dateTimeStart", dateTimeStart);
        mav.addObject("dateTimeEnd", dateTimeEnd);
        return mav;
    }

    private Map<String, Integer> count(File file, boolean flag, String str) {
        BufferedReader reader = null;
        Date yesterday = new Date(hasoffer.base.utils.TimeUtils.yesterday());
        Map<String, Integer> countMap = new ConcurrentHashMap<>();
        int count = 0;

        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                if (!tempString.contains("GET")) {
                    continue;
                }
                int s = tempString.indexOf("[");
                int e = tempString.indexOf("]");
                String time = tempString.substring(s + 1, e);

                String reqUrl = tempString.split("GET")[1];

                Date logTime = TimeUtils.parseUSDate(time, DATE_PATTERN);
                if (TimeUtils.getStartTimeOfDate(yesterday).getTime() < logTime.getTime() && logTime.getTime() < TimeUtils.getEndTimeOfDate(yesterday).getTime()) {
                    if (reqUrl.contains("ad=")) {
                        String adStr = reqUrl.split("ad=")[1];
                        int ind = adStr.indexOf("&");
                        String mid = null;
                        if (ind > 0) {
                            mid = adStr.substring(0, ind);
                        } else {
                            mid = adStr.split(" ")[0];
                        }
                        if (countMap.containsKey(mid)) {
                            countMap.put(mid, countMap.get(mid) + 1);
                        } else {
                            countMap.put(mid, 1);
                        }
                    }

                    if (flag) {
                        if (reqUrl.contains(str)) {
                            count++;
                        }
                    } else {
                        count++;
                    }
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        countMap.put("count", count);
        return countMap;
    }

    private Map<String, Map<String, Integer>> mapCombine(Map<String, Integer> dateMap, Map<String, Map<String, Integer>> result, String mapType) {
        Iterator<String> it = dateMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals("count")) {
                continue;
            }
            if (!result.containsKey(key)) {
                Map<String, Integer> m = new HashMap<>();
                m.put(mapType, dateMap.get(key));
                result.put(key, m);
            } else {
                result.get(key).put(mapType, dateMap.get(key));
            }
        }
        return result;
    }

    @RequestMapping(value = "/data/detail", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson detail(@RequestParam(value = "date") String dateStr) {

        List<AccessLogDetailVo> list = accessLogService.findLogDetail(dateStr);
        return new AjaxJson(Constants.HttpStatus.OK, list);
    }
}

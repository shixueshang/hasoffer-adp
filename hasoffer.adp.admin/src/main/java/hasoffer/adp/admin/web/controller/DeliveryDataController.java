package hasoffer.adp.admin.web.controller;

import hasoffer.adp.admin.web.configuration.RootConfiguration;
import hasoffer.adp.base.utils.TimeUtils;
import hasoffer.adp.base.utils.mail.MailSenderInfo;
import hasoffer.adp.base.utils.mail.SimpleMailSender;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.base.utils.page.PageHelper;
import hasoffer.adp.core.models.po.AccessLog;
import hasoffer.adp.core.service.AccessLogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

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

        int reqs = this.count(new File(requestPath), false, null);
        int clicks = this.count(new File(adclickPath), false, null);
        int imgs = this.count(new File(adimgPath), false, null);
        int pvCall = this.count(new File(callbackPath), true, "/pv");
        int pvClick = this.count(new File(callbackPath), true, "/click");

        AccessLog log = new AccessLog();
        log.setDate(new Date(hasoffer.base.utils.TimeUtils.yesterday()));
        log.setRequests(reqs);
        log.setPvCallback(pvCall);
        log.setPvClicks(pvClick);
        log.setImgRequests(imgs);
        log.setClicks(clicks);

        System.out.println("start insert log......");
        accessLogService.insert(log);


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
        String content = "请求数 : " + reqs + "\n" + "pv回调数 : " + pvCall + "\n" + "pv点击数 : " + pvClick + "\n" + "图片请求数 : " + imgs + "\n" + "点击数 : " + clicks;
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
        return "setting/dataCount";
    }

    @RequestMapping(value = "/data/find", method = RequestMethod.GET)
    public ModelAndView dataCount(@RequestParam(value = "dateTimeStart") Date dateTimeStart, @RequestParam(value = "dateTimeEnd") Date dateTimeEnd) {
        ModelAndView mav = new ModelAndView("/setting/dataCount");
        Page<AccessLog> pageResult = accessLogService.findPage(page, size, dateTimeStart, dateTimeEnd);
        mav.addObject("page", PageHelper.getPageModel(request, pageResult));
        mav.addObject("logs", pageResult.getItems());
        mav.addObject("dateTimeStart", dateTimeStart);
        mav.addObject("dateTimeEnd", dateTimeEnd);
        return mav;
    }

    private int count(File file, boolean flag, String str) {
        BufferedReader reader = null;
        Date yesterday = new Date(hasoffer.base.utils.TimeUtils.yesterday());
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

        return count;
    }
}

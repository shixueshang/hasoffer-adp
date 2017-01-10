package hasoffer.adp.admin.web.controller;

import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihongde on 2017/1/10 11:28
 */
@Controller
public class DeliveryDataController extends BaseController {

    private static final String DATE_PATTERN = "dd/MMM/yyyy:HH:mm:ss";


    @RequestMapping(value = "/data/list")
    public String countPage() {
        return "setting/dataCount";
    }

    @RequestMapping(value = "/data/find", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson dataCount(@RequestParam(value = "dateTimeStart") Date dateTimeStart, @RequestParam(value = "dateTimeEnd") Date dateTimeEnd) {

        String reqPath = "F:\\work\\hasoffer\\log\\request\\access.log";
        String clickPath = "F:\\work\\hasoffer\\log\\click\\access.log";
        String imgPath = "F:\\work\\hasoffer\\log\\img\\access.log";
        String pvPath = "F:\\work\\hasoffer\\log\\pv\\access.log";

        File reqFile = new File(reqPath);
        File clickFile = new File(clickPath);
        File imgFile = new File(imgPath);
        File pvFile = new File(pvPath);

        int reqs = this.count(reqFile, dateTimeStart, dateTimeEnd, false, null);
        int clicks = this.count(clickFile, dateTimeStart, dateTimeEnd, false, null);
        int imgs = this.count(imgFile, dateTimeStart, dateTimeEnd, false, null);
        int pvCall = this.count(pvFile, dateTimeStart, dateTimeEnd, true, "/pv");
        int clickCall = this.count(pvFile, dateTimeStart, dateTimeEnd, true, "/click");

        Map<String, Integer> result = new HashMap<>();
        result.put("reqCount", reqs);
        result.put("clickCount", clicks);
        result.put("imgCount", imgs);
        result.put("pvCallCount", pvCall);
        result.put("clickCallCount", clickCall);

        return new AjaxJson(Constants.HttpStatus.OK, result);
    }

    private int count(File file, Date start, Date end, boolean flag, String str) {
        BufferedReader reader = null;
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
                if (start.before(logTime) && logTime.before(end)) {
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
                }
            }
        }

        return count;
    }
}

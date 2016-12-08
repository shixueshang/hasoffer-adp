package hasoffer.adp.admin.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lihongde on 2016/12/1 12:16
 */
@Controller
public class LoginController extends BaseController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginPost(String username, String password) {
        ModelAndView mav = new ModelAndView();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            mav.setViewName("index");
            mav.addObject("error", "用户名/密码不能为空");
            return mav;
        }
        if(!"admin".equals(username)){
            mav.setViewName("index");
            mav.addObject("error", "用户名输入错误");
            return mav;
        }
        if(!"xing*.=".equals(password)){
            mav.setViewName("index");
            mav.addObject("error", "密码输入错误");
            return mav;
        }
        try {
            mav.setViewName("home");
            return mav;
        } catch (RuntimeException e) {
            mav.setViewName("index");
            mav.addObject("error", "未知错误，请联系管理员");
            return mav;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {

        return "index";
    }
}

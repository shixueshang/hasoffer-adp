package hasoffer.adp.admin.web.controller;

import hasoffer.adp.core.models.po.HotDealPo;
import hasoffer.adp.core.service.HotDealService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chevy on 16-11-19.
 */
@Controller
@RequestMapping(value = "/hd")
public class HotDealController {

    @Resource
    HotDealService hotDealService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list_hds(HttpServletRequest request,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "0") int size) {

        ModelAndView mav = new ModelAndView("hd/list");

        HotDealPo hd = hotDealService.find(1000);

        mav.addObject("hd", hd);

        return mav;
    }


}

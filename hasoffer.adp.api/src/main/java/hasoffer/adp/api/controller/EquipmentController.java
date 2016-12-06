package hasoffer.adp.api.controller;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.base.utils.page.PageHelper;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2016/12/2 10:45
 */
@Controller
@RequestMapping(value = "/equipment")
public class EquipmentController extends BaseController {

    @Resource
    EquipmentService equipmentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView listEquip(@RequestParam(value = "size", defaultValue = "1000") int defaultSize){
        ModelAndView mav = new ModelAndView();
        Page<Equipment> pageResult = equipmentService.findPage(page, defaultSize);
        mav.addObject("page", PageHelper.getPageModel(request, pageResult));
        mav.addObject("equipments", pageResult.getItems());
        return mav;
    }
}

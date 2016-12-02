package hasoffer.adp.admin.web.controller;

import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.service.EquipmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2016/12/2 10:45
 */
@Controller
@RequestMapping(value = "/equipment")
public class EquipmentController extends BaseController {

    @Resource
    EquipmentService equipmentService;

    @RequestMapping(value = "/listJson", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson listJsonEquip(){

        Page<Equipment> result = equipmentService.findPage(page, size);
        return new AjaxJson(Constants.HttpStatus.OK, result);
    }
}

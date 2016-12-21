package hasoffer.adp.api.controller;

import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.service.EquipmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2016/12/2 10:45
 */
@RestController
@RequestMapping(value = "/equipment")
public class EquipmentController extends BaseController {

    @Resource
    EquipmentService equipmentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public AjaxJson listEquip(@RequestParam(value = "size", defaultValue = "1000") int defaultSize){
        return new AjaxJson(Constants.HttpStatus.OK, equipmentService.findPage(page, defaultSize));
    }
}

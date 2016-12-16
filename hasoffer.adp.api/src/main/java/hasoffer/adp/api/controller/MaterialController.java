package hasoffer.adp.api.controller;

import hasoffer.adp.api.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import hasoffer.adp.core.models.vo.MaterialVo;
import hasoffer.adp.core.service.MaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihongde on 2016/12/1 11:10
 */
@Controller
@RequestMapping(value = "/material")
public class MaterialController extends BaseController{

    @Resource
    private MaterialService materialService;

    @Resource
    RootConfiguration configuration;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public AjaxJson listMarerial(){
        List<MaterialVo> mvList = new ArrayList<>();
        Page<Material> pageResult = materialService.findPage(page, size);
        List<Material> list = pageResult.getItems();
        for(Material m : list){
            if(!StringUtils.isEmpty(m.getIcon())){
                m.setIcon(configuration.getDomainUrl() + m.getIcon());
            }
            List<MaterialCreative> creatives = materialService.findCreatives(m.getId());
            for(MaterialCreative mcv : creatives){
                mcv.setUrl(configuration.getDomainUrl() + mcv.getUrl());
            }
            m.setCreatives(creatives);
            MaterialVo mv = new MaterialVo();
            BeanUtils.copyProperties(m, mv);
            mvList.add(mv);
        }

        return new AjaxJson(Constants.HttpStatus.OK, new Page<MaterialVo>(page, size, pageResult.getTotal(), mvList));
    }


}

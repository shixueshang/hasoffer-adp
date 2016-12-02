package hasoffer.adp.admin.web.controller;

import com.alibaba.fastjson.JSON;
import hasoffer.adp.admin.web.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.base.utils.page.PageHelper;
import hasoffer.adp.core.enums.AndroidVersion;
import hasoffer.adp.core.enums.AppType;
import hasoffer.adp.core.enums.IOSVersion;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

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
    public ModelAndView listMarerial(){
        ModelAndView mav = new ModelAndView("/material/list");
        Page<Material> pageResult = materialService.findPage(page, size);
        List<Material> list = pageResult.getItems();
        for(Material m : list){
            if(!StringUtils.isEmpty(m.getIcon())){
                m.setIcon(configuration.getDomainUrl() + m.getIcon());
            }
            if(!StringUtils.isEmpty(m.getOtherIcon())){
                m.setOtherIcon(configuration.getDomainUrl() + m.getOtherIcon());
            }
        }
        mav.addObject("page", PageHelper.getPageModel(request, pageResult));
        mav.addObject("materials", list);
        mav.addObject("url", "material");
        return mav;
    }

    @RequestMapping(value = "/create")
    public String create(Model model){
        model.addAttribute("androidVersions", JSON.toJSON(AndroidVersion.bulidAndroidVersion()));
        model.addAttribute("IOSVersions", JSON.toJSON(IOSVersion.bulidIOSVersion()));
        model.addAttribute("appTypes", AppType.buildAppTypes());
        model.addAttribute("url", "material");
        return "material/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveOrUpdate(Material material, MultipartFile iconFile, MultipartFile otherIconFile){
        if(iconFile != null && !iconFile.isEmpty()){
            String originName = iconFile.getOriginalFilename();
            String suffix = originName.substring(originName.lastIndexOf(".") + 1);
            String name = UUID.randomUUID().toString() + "." + suffix;
            String iconPath = configuration.getImagePathDir();
            this.transferFile(iconPath, name, iconFile);
            material.setIcon(name);
        }
        if(otherIconFile != null && !otherIconFile.isEmpty()){
            String originName = otherIconFile.getOriginalFilename();
            String suffix = originName.substring(originName.lastIndexOf(".") + 1);
            String name = UUID.randomUUID().toString() + "." + suffix;
            String otherIconPath = configuration.getImagePathDir();
            this.transferFile(otherIconPath, name, iconFile);
            material.setOtherIcon(name);
        }

        material.setPvRequestUrl(configuration.getPvRequestUrl());
        if(material.getId() == null){
            materialService.insert(material);
        }else{
            materialService.update(material);
        }
        return "redirect:/material/list";
    }

    @RequestMapping(value = "/listJson", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson listJsonMarerial(){

        Page<Material> result = materialService.findPage(page, size);
        List<Material> list = result.getItems();
        for(Material m : list){
            if(!StringUtils.isEmpty(m.getIcon())){
                m.setIcon(configuration.getDomainUrl() + m.getIcon());
            }
            if(!StringUtils.isEmpty(m.getOtherIcon())){
                m.setOtherIcon(configuration.getDomainUrl() + m.getOtherIcon());
            }
        }
        return new AjaxJson(Constants.HttpStatus.OK, result);
    }

    private static void transferFile(String path, String fileName, MultipartFile file) {
        File targetFile = new File(path, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package hasoffer.adp.admin.web.controller;

import com.alibaba.fastjson.JSON;
import hasoffer.adp.admin.web.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.base.utils.page.PageHelper;
import hasoffer.adp.core.enums.AppType;
import hasoffer.adp.core.enums.Tags;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import hasoffer.adp.core.service.MaterialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by lihongde on 2016/12/1 11:10
 */
@Controller
@RequestMapping(value = "/material")
public class MaterialController extends BaseController{

    @Resource
    RootConfiguration configuration;
    @Resource
    private MaterialService materialService;

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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView listMarerial(){
        ModelAndView mav = new ModelAndView("/material/list");
        Page<Material> pageResult = materialService.findPage(page, size);
        List<Material> list = pageResult.getItems();
        for(Material m : list){
            if(!StringUtils.isEmpty(m.getIcon())){
                m.setIcon(configuration.getDomainUrl() + m.getIcon());
            }
            List<MaterialCreative> creatives = materialService.findCreatives(m.getId());
            for(MaterialCreative mv : creatives){
                mv.setUrl(configuration.getDomainUrl() + mv.getUrl());
            }
            m.setCreatives(creatives);
        }
        mav.addObject("page", PageHelper.getPageModel(request, pageResult));
        mav.addObject("materials", list);
        mav.addObject("url", "material");
        return mav;
    }

    @RequestMapping(value = "/create")
    public String create(Model model){
        model.addAttribute("appTypes", AppType.buildAppTypes());
        model.addAttribute("tags", Tags.bulidTags());
        model.addAttribute("url", "material");
        return "material/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveOrUpdate(Material material, MultipartFile iconFile){
        if(iconFile != null && !iconFile.isEmpty()){
            String originName = iconFile.getOriginalFilename();
            String suffix = originName.substring(originName.lastIndexOf(".") + 1);
            String newName = UUID.randomUUID().toString() + "." + suffix;
            String iconPath = configuration.getImagePathDir();
            this.transferFile(iconPath, newName, iconFile);
            material.setIcon(newName);
        }

        material.setPvRequestUrl(configuration.getPvRequestUrl());
        if(material.getId() == null){
            materialService.insert(material);
        }else{
            materialService.update(material);
        }

        String otherIcons = material.getOtherIcons();
        if(!StringUtils.isEmpty(otherIcons)){
            String[] creatives = otherIcons.split(",");
            for(String imgName : creatives){
                String wh = imgName.split("_")[1];
                String width = wh.split("x")[0];
                String height = wh.split("x")[1];
                MaterialCreative mc = new MaterialCreative(material.getId(), imgName, width, height);
                materialService.insertCreatives(mc);
            }
        }
        return "redirect:/material/list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String edit(@PathVariable(value = "id") Long id, Model model){
        Material material = materialService.find(id);
        List<MaterialCreative> creatives = materialService.findCreatives(id);
        for(MaterialCreative mv : creatives){
            mv.setUrl(configuration.getDomainUrl() + mv.getUrl());
        }

        String appTypes = material.getAppType();
        List<String> checkedApps = Arrays.asList(appTypes.split(","));
        String tags = material.getTags();
        List<String> checkedTags = Arrays.asList(tags.split(","));
        model.addAttribute("appTypes", AppType.buildAppTypes());
        model.addAttribute("tags", Tags.bulidTags());
        model.addAttribute("checkedApps", checkedApps);
        model.addAttribute("checkedTags", checkedTags);
        model.addAttribute("material", material);
        model.addAttribute("creatives", JSON.toJSON(creatives));
        return "material/add";
    }

    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson upload(MultipartFile[] creativeFile) throws IOException{
        List<String> fileNameList = new ArrayList<String>();
        for(MultipartFile file : creativeFile){
            String originName = file.getOriginalFilename();
            String suffix = originName.substring(originName.lastIndexOf(".") + 1);

            BufferedImage sourceImg  = ImageIO.read(file.getInputStream());
            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();

            String newName = UUID.randomUUID().toString() + "_" + width + "x" + height + "_" +"." + suffix;
            String path = configuration.getImagePathDir();
            transferFile(path, newName, file);
            fileNameList.add(newName);
        }

        return new AjaxJson(Constants.HttpStatus.OK, fileNameList);

    }

    @RequestMapping(value = "/changeDelivery/{id}")
    public String changeDelivery(@PathVariable(value = "id") Long id) {
        Material m = materialService.find(id);
        Integer isDelivery = m.getIsDelivery();
        m.setIsDelivery(isDelivery == 1 ? 0 : 1);

        materialService.update(m);

        return "redirect:/material/list";
    }
}

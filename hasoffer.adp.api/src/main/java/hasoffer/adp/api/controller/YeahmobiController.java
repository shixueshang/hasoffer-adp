package hasoffer.adp.api.controller;

import hasoffer.adp.api.configuration.RootConfiguration;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.MaterialService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/21 17:59
 */
@RestController
@RequestMapping(value = "/ym")
public class YeahmobiController extends BaseController {

    @Resource
    RootConfiguration configuration;
    @Resource
    private MaterialService materialService;
    @Resource
    private EquipmentService equipmentService;

    /**
     * 提供获得广告素材接口
     * @param country
     * @param androidid
     * @param gaid google advertiser id
     * @param width
     * @param height
     * @return
     */
    @RequestMapping(value = "/getAd", method = RequestMethod.GET)
    public Map<String, Object> bidForYeahmobi(@RequestParam(value = "country", defaultValue = "IN") String country,
                                              @RequestParam(value = "aid", required = false) String androidid,
                                              @RequestParam(value = "gaid", required = false) String gaid,
                                              @RequestParam(value = "imgw") int width,
                                              @RequestParam(value = "imgh") int height){

        String msg = "No matching material found";
        Map<String, Object> result = new ConcurrentHashMap<>();
        if(StringUtils.isEmpty(androidid)){
            result.put("error_msg" ,msg);
            return result;
        }

        Equipment eq = equipmentService.findByAndroidid(androidid);
        if(eq == null){
            result.put("error_msg" ,msg);
            return result;
        }

        String[] tags = eq.getTags().split(",");
        List<Material> ms = materialService.findLikeByTag(tags[0]);
        if(ms.size() == 0){
            result.put("error_msg" ,msg);
            return result;
        }

//        FlipkartHelper.getUrlWithAff(url, new String[]{"HASAD_YM", androidid});

        Material m = ms.get(0);
        result.put("error_msg","ok");
        result.put("titel", m.getTitle());
        result.put("desc", m.getDescription());
        result.put("img", configuration.getDomainUrl() + m.getCreatives().get(0).getUrl());
        result.put("imgw", m.getCreatives().get(0).getWidth());
        result.put("imgh", m.getCreatives().get(0).getHeight());
        if (StringUtils.isEmpty(m.getIcon())) {
            result.put("icon", "");
        } else {
            result.put("icon", configuration.getDomainUrl() + m.getIcon());
        }
        result.put("clk_url", m.getUrl());
        result.put("btn_text", m.getBtnText());
        result.put("imp_tks", new String[]{m.getPvRequestUrl()});
        result.put("clk_tks", new String[]{"http://adclick.hasoffer.cn"});
        return result;
    }
}

package hasoffer.adp.admin.web.controller;


import com.alibaba.fastjson.JSON;
import hasoffer.adp.admin.web.configuration.RootConfiguration;
import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.vo.AdResultPo;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.MaterialService;
import hasoffer.base.utils.StringUtils;
import hasoffer.data.redis.IRedisMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lihongde on 2016/12/22 16:09
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController extends BaseController {

    @Resource
    IRedisMapService redisMapService;

    @Resource
    EquipmentService equipmentService;

    @Resource
    MaterialService materialService;

    @Resource
    RootConfiguration configuration;

    @RequestMapping(value = "/loadRedisData", method = RequestMethod.GET)
    public String task(Model model) {
        model.addAttribute("url", "/task");

        return "task";
    }

    /**
     * 1、查询所有的设备，按照{aid=tags}格式加载到redis中
     * 2、查询所有素材，按照{tag=mid1, mid2}格式加载到redis
     * 3、查询所有素材，按照{mid=mobj}格式加载到redis
     * 4、封装result加载到redis
     */
    @RequestMapping(value = "/execute", method = RequestMethod.GET)
    @ResponseBody
    public AjaxJson execute() {

        try {
            List<Equipment> eqs = equipmentService.findAllEquips();
            Map<String, String> amap = new HashMap<>();
            for (Equipment e : eqs) {
                amap.put(e.getAndroidId(), e.getTags());
            }

            redisMapService.putMap(Constants.REDIS_MAP_KEY.AIDTAGMAP, amap);


            List<Material> list = materialService.findAllMaterials();
            Map<String, String> mtmap = new HashMap<>();
            for (Material m : list) {
                String tags = m.getTags();
                if (tags != null) {
                    String[] tagArr = tags.split(",");
                    for (String t : tagArr) {
                        mtmap.put(t, m.getId().toString());
                    }
                }
            }

            Map<String, String> tagMap = new HashMap<>();
            this.paraseMap(mtmap, tagMap);

            redisMapService.putMap(Constants.REDIS_MAP_KEY.MATTAGMAP, tagMap);


            List<Material> mlist = materialService.findAllMaterials();
            for (Material m : mlist) {

                AdResultPo arp = new AdResultPo();
                arp.setTitle(m.getTitle());
                arp.setDesc(m.getDescription());
                arp.setImg(configuration.getDomainUrl() + m.getCreatives().get(0).getUrl());
                arp.setImgw(m.getCreatives().get(0).getWidth());
                arp.setImgh(m.getCreatives().get(0).getHeight());
                if (!StringUtils.isEmpty(m.getIcon())) {
                    arp.setIcon(configuration.getDomainUrl() + m.getIcon());
                } else {
                    arp.setIcon("");
                }

                arp.setClk_url(m.getUrl());
                arp.setBtn_text(m.getBtnText());
                arp.setImp_tks(new String[]{m.getPvRequestUrl()});
                arp.setClk_tks(new String[]{configuration.getClickUrl()});

                String s = JSON.toJSONString(arp);

                redisMapService.putMap(Constants.REDIS_MAP_KEY.MRESULT, m.getId().toString(), s);
            }
            System.out.println("load data success...");
            return new AjaxJson(Constants.HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("load data failed...");
            e.printStackTrace();
            return new AjaxJson(Constants.HttpStatus.SERVER_ERROR);
        }

    }

    /**
     * 解析map{tag=mid} 得到新的map{tag="mid1, mid2"}并且mid不重复
     *
     * @param mtmap
     * @param tagMap
     */
    private void paraseMap(Map<String, String> mtmap, Map<String, String> tagMap) {
        for (Map.Entry<String, String> entry : mtmap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (tagMap.containsKey(key)) {
                if (!tagMap.get(key).contains(value)) {
                    value = tagMap.get(key) + "," + value;
                }
            }

            tagMap.put(key, value);
        }
    }
}

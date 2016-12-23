package hasoffer.adp.core.core.test.db;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.vo.AdResultPo;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.MaterialService;
import hasoffer.data.redis.IRedisMapService;
import hasoffer.data.redis.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongde on 2016/12/22 12:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestRedis {

    @Resource
    IRedisMapService redisMapService;

    @Resource
    IRedisService redisService;

    @Resource
    EquipmentService equipmentService;

    @Resource
    MaterialService materialService;

    /**
     * 解析map{tag=mid} 得到新的map{tag="mid1, mid2"}并且mid不重复
     *
     * @param mtmap
     * @param tagMap
     */
    private static void paraseMap(Map<String, String> mtmap, Map<String, String> tagMap) {
        for (Map.Entry<String, String> entry : mtmap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (tagMap.containsKey(key)) {
                if (!tagMap.get(key).contains(value)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(tagMap.get(key)).append(",").append(value);
                    value = sb.toString();
                }
            }

            tagMap.put(key, value);
        }
    }

    @Test
    public void loadTags() {

        List<Equipment> eqs = equipmentService.findAllEquips();
        Map<String, String> amap = new HashMap<>();
        for (Equipment e : eqs) {
            amap.put(e.getAndroidId(), e.getTags());
        }

        redisMapService.putMap("AIDTAGMAP", amap);

    }

    @Test
    public void loadTagMaterial() {
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
        paraseMap(mtmap, tagMap);

        redisMapService.putMap("MATTAGMAP", tagMap);

    }

    @Test
    public void testGet() {
        Map<String, Object> map = redisMapService.getMap("AIDTAGMAP");
        System.out.println(map.size());
    }

    @Test
    public void getMatTagData() {
        Map<String, String> map = redisMapService.getMap("MATTAGMAP");
        System.out.println(map);
    }

    @Test
    public void loadMaterial() {
        List<Material> list = materialService.findAllMaterials();
        for (Material m : list) {

            System.out.println(m.getId());
            redisService.add(m.getId().toString(), m, 0);

            AdResultPo arp = new AdResultPo();
            arp.setTitle(m.getTitle());
            arp.setDesc(m.getDescription());
            arp.setImg("");
            arp.setImgw(m.getCreatives().get(0).getWidth());
            arp.setImgh(m.getCreatives().get(0).getHeight());
            arp.setIcon("");
            arp.setClk_url(m.getUrl());
            arp.setBtn_text(m.getBtnText());
            arp.setImp_tks(new String[]{m.getPvRequestUrl()});
            System.out.println(m.getId());

            String s = JSON.toJSONString(arp);

            String ms = JSON.toJSONString(m);

            redisMapService.putMap("MATMAP", m.getId().toString(), ms);
            redisMapService.putMap("MRESULT", m.getId().toString(), s);
        }
    }

    @Test
    public void getMaterial() throws IOException {

        Object m = redisMapService.getValue("MATMAP", "2");

        System.out.println(m);


        Object rr = redisMapService.getValue("MRESULT", "2");

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(rr.toString(), Map.class);

        System.out.println(map);



    }
}

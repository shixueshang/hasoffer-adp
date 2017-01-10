package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.HttpPostGet;
import hasoffer.adp.base.utils.MapValueComparator;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Tag;
import hasoffer.adp.core.models.po.Tag2;
import hasoffer.adp.core.models.po.TagStatistical;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by lihongde on 2016/12/2 11:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CoreConfiguration.class)
//@Transactional(transactionManager = "txManager")
public class TestEquipment {

    @Resource
    EquipmentService equipmentService;

    @Resource
    TagService tagService;

    @Test
    public void insertTag(){
        equipmentService.truncate();
        List<TagStatistical> list = tagService.findAllTags();
        List<Equipment> data = new ArrayList<>();
        for(TagStatistical t : list){
            StringBuilder sb = new StringBuilder();
            if(t.getXiaomi() > 0){
                sb.append("xiaomi,");
            }
            if(t.getLenovo() > 0){
                sb.append("lenovo,");
            }
            if(t.getRedmi() > 0){
                sb.append("redmi,");
            }
            if(t.getHuawei() > 0){
                sb.append("huawei,");
            }
            if(t.getHonor() > 0){
                sb.append("honor,");
            }
            if(t.getSamsung() > 0){
                sb.append("samsung,");
            }
            if(t.getMeizu() > 0){
                sb.append("meizu,");
            }
            String tag = "";
            if(!sb.toString().equals("")){
                tag = sb.substring(0, sb.length()- 1);
            }

            Equipment e = new Equipment(t.getAndroidid(), tag);
            //equipmentService.insert(e);
            data.add(e);
        }
        System.out.println("start insert tags ..." + new Date());
        equipmentService.batchInsert(data);

        System.out.println("insert tags end ..." + new Date());

    }

    @Test
    public void insertEq() {

        equipmentService.truncate();
        List<Tag> tags = tagService.findTagsGroupByAid();

        List<Equipment> list = new ArrayList<>();

        Map<String, Map<String, Integer>> map = new HashMap<>();
        for (Tag t : tags) {
            Map<String, Integer> tmap = new HashMap<>();
            if (t.getXiaomi() > 0) {
                tmap.put("xiaomi", t.getXiaomi());
            }
            if (t.getLenovo() > 0) {
                tmap.put("lenovo", t.getLenovo());
            }
            if (t.getRedmi() > 0) {
                tmap.put("redmi", t.getRedmi());
            }
            if (t.getLeeco() > 0) {
                tmap.put("leeco", t.getLeeco());
            }
            if (t.getSamsung() > 0) {
                tmap.put("samsung", t.getSamsung());
            }
            if (t.getMoto() > 0) {
                tmap.put("moto", t.getMoto());
            }

            map.put(t.getAid(), tmap);
        }


        for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
            String aid = entry.getKey();
            Map<String, Integer> tagMap = entry.getValue();
            MapValueComparator mc = new MapValueComparator(tagMap);
            Map<String, Integer> sortedMap = new TreeMap<String, Integer>(mc);
            sortedMap.putAll(tagMap);

            StringBuilder tagStr = new StringBuilder();
            for (Map.Entry<String, Integer> tentry : sortedMap.entrySet()) {
                tagStr.append(tentry.getKey()).append(",");
            }
            if (!tagStr.toString().equals("")) {
                String tag = tagStr.substring(0, tagStr.length() - 1);

                Equipment e = new Equipment(aid, tag);

                list.add(e);
            }

        }

        System.out.println("start insert tags ..." + new Date());
        equipmentService.batchInsert(list);

        System.out.println("insert tags end ..." + new Date());

    }

    @Test
    public void testMatchSamsung() {
        final String url = "http://ad.hasoffer.cn/ym/getAd?imgw=100&imgh=1000&aid=%s";
        List<Equipment> eqs = equipmentService.findAllEquips();
        for (Equipment eq : eqs) {
            if (!eq.getTags().equals("samsung")) {
                continue;
            }

            String aid = eq.getAndroidId();

            String real_url = String.format(url, aid);

            HttpPostGet h = new HttpPostGet();
            try {
                String re = h.sendGet(real_url);
                int code = h.getResponseCode();

                System.out.println(String.format("response[%d] : %s", code, re));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void insertTag2() {
        //equipmentService.truncate();
        List<Tag2> list = tagService.findAllTag2s();
        List<Equipment> data = new ArrayList<>();
        for (Tag2 t : list) {
            StringBuilder sb = new StringBuilder();
            if (t.getOnePlus_3T() > 0) {
                sb.append("OnePlus_3T,");
            }
            if (t.getMoto_G_Plus_4th_Gen() > 0) {
                sb.append("Moto_G_Plus_4th_Gen,");
            }
            if (t.getLenovo_Vibe_K5_Note() > 0) {
                sb.append("Lenovo_Vibe_K5_Note,");
            }
            if (t.getLeEco_Le_1s_Eco() > 0) {
                sb.append("LeEco_Le_1s_Eco,");
            }
            if (t.getMoto_M() > 0) {
                sb.append("Moto_M,");
            }
            if (t.getLenovo_Phab_2() > 0) {
                sb.append("Lenovo_Phab_2,");
            }
            if (t.getPanasonic_Eluga_Note() > 0) {
                sb.append("Panasonic_Eluga_Note,");
            }
            if (t.getSAMSUNG_Galaxy_On8() > 0) {
                sb.append("SAMSUNG_Galaxy_On8,");
            }
            if (t.getSAMSUNG_Galaxy_On_Nxt() > 0) {
                sb.append("SAMSUNG_Galaxy_On_Nxt,");
            }
            if (t.getYu_Yureka_Plus() > 0) {
                sb.append("Yu_Yureka_Plus,");
            }
            String tag = "";
            if (!sb.toString().equals("")) {
                tag = sb.substring(0, sb.length() - 1);
            }

            Equipment e = new Equipment(t.getAid(), tag);
            data.add(e);
        }
        System.out.println("start insert tags ..." + new Date());
        equipmentService.batchInsert(data);

        System.out.println("insert tags end ..." + new Date());

    }

}

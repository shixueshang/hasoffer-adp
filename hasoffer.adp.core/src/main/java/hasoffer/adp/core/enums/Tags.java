package hasoffer.adp.core.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/21 10:36
 */
public enum Tags {

    xiaomi, lenovo, redmi, moto, leeco, samsung, OnePlus_3T, Moto_G_Plus_4th_Gen, Lenovo_Vibe_K5_Note, LeEco_Le_1s_Eco, Moto_M, Lenovo_Phab_2, Panasonic_Eluga_Note, SAMSUNG_Galaxy_On8, SAMSUNG_Galaxy_On_Nxt, Yu_Yureka_Plus;

    public static List<Map<String, Object>> bulidTags(){
        List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
        Tags[] ts =  Tags.values();
        for(Tags t : ts){
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("name", t.name());
            tags.add(map);
        }
        return tags;
    }
}

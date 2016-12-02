package hasoffer.adp.core.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/11/29 15:51
 */
public enum AppType {
    Books,
    Business,
    Catalogs,
    Education,
    Entertainment,
    Finance,
    Food_Drink,
    Games,
    Health_Fitness,
    Lifestyle,
    Medical,
    Music,
    Navigation,
    News,
    Photo_Video,
    Productivity,
    Reference,
    Social_Networking,
    Sports,
    Travel,
    Utilities,
    Weather,
    Adult;

    public static List<Map<String, Object>> buildAppTypes(){
        List<Map<String, Object>> types = new ArrayList<>();
        AppType[] arr = AppType.values();
        for(AppType ap : arr ){
            Map<String, Object> map = new ConcurrentHashMap<>();
            if(ap.name().contains("_")){
                map.put("name", ap.name().replace("_", "&"));
            }else{
                map.put("name", ap.name());
            }
            types.add(map);
        }
        return types;
    }
}

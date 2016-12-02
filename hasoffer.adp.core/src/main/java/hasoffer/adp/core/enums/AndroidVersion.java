package hasoffer.adp.core.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/11/29 15:59
 */
public enum AndroidVersion {
    Lower4_1("<4.1", 1), Lower4_3("4.1<=&<=4.3", 2), Lower5("4.4<=&<5.0", 3), Lower6("5.0<=&<6.0", 4), Lower7("6.0<=&<7.0", 5), LowerOrEqual7("<=7.0", 6);

    private String name;
    private int index;

    private AndroidVersion(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (AndroidVersion c : AndroidVersion .values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static List<Map<String, Object>> bulidAndroidVersion(){
        List<Map<String, Object>> versions = new ArrayList<Map<String, Object>>();
        AndroidVersion[] avs =  AndroidVersion.values();
        for(AndroidVersion av : avs){
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("name", av.getName());
            map.put("index", av.getIndex());
            versions.add(map);
        }
        return versions;
    }
}

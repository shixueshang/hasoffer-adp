package hasoffer.adp.core.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/11/29 16:05
 */
public enum IOSVersion {
    Lower6("<6.0", 1), Lower7("6.0<=&<7.0", 2), Lower8("7.0<=&<8.0", 3), Lower9("8.0<=&<9.0", 4), Lower10("9.0<=&<10.0", 5), LowerOrEqual10("<=10.0", 6);

    private String name;
    private int index;

    private IOSVersion(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static String getName(int index) {
        for (IOSVersion c : IOSVersion .values()) {
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

    public static List<Map<String, Object>> bulidIOSVersion(){
        List<Map<String, Object>> versions = new ArrayList<Map<String, Object>>();
        IOSVersion[] ivs =  IOSVersion.values();
        for(IOSVersion iv : ivs){
            Map<String, Object> map = new ConcurrentHashMap<>();
            map.put("name", iv.getName());
            map.put("index", iv.getIndex());
            versions.add(map);
        }
        return versions;
    }
}

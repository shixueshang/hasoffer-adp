package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.FileUtil;
import hasoffer.adp.core.models.po.TagStatistical;

import java.io.File;
import java.util.*;

/**
 * Created by lihongde on 2016/12/2 15:39
 */
public class Test {

    public static void main(String[] args) {
        List<Map<String, String>> list = new ArrayList<>();
        String path = "F:\\work\\hasoffer\\bb";
        File[] files = FileUtil.getFiles(path);
        for(File file : files){
           Map<String, String> fileMap =  FileUtil.readFileByLines(file);
            list.add(fileMap);
        }

        Map<String, List<String>> tagMap = new HashMap<>();
        for(Map<String, String> mapData : list){
             paraseMap(mapData, tagMap);
        }

        List<TagStatistical> result = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry : tagMap.entrySet()){
            String android = entry.getKey();
            List<String> tags = entry.getValue();
            result.add(addTag(android, tags));
        }

        for(TagStatistical t : result){
            if(t.getAndroidid() == "2c2b436320a041ad"){
                System.out.println("androidid : " + t.getAndroidid() + " xiaomi : " + t.getXiaomi() + " lenovo : " + t.getLenovo() +
                        " redmi : " + t.getRedmi() + " huawei : " + t.getHuawei() + " honor : " + t.getHonor() + " samsung : " + t.getSamsung() + " meizu : " + t.getMeizu());

            }
        }

    }

    private static void paraseMap(Map<String, String> mapData,  Map<String, List<String>> tagMap){
        for(Map.Entry<String, String> entry : mapData.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            if(!tagMap.containsKey(key)){
                List newList = new ArrayList<>();
                newList.add(value);
                tagMap.put(key, newList);
            }else{
                tagMap.get(key).add(value);
            }
        }
    }

    private static TagStatistical addTag(String android, List<String> tagString){
        TagStatistical tag = new TagStatistical(android);
        for(String value : tagString){
            if(value.toLowerCase().contains("xiaomi")){
                tag.setXiaomi(tag.getXiaomi() + 1);
            }else if(value.toLowerCase().contains("lenovo")){
                tag.setLenovo(tag.getLenovo() + 1);
            }else if(value.toLowerCase().contains("redmi")){
                tag.setRedmi(tag.getRedmi() + 1);
            }else if(value.toLowerCase().contains("huawei")){
                tag.setHuawei(tag.getHuawei() + 1);
            }else if(value.toLowerCase().contains("honor")){
                tag.setHonor(tag.getHonor() + 1);
            }else if(value.toLowerCase().contains("samsung")){
                tag.setSamsung(tag.getSamsung() + 1);
            }else{
                tag.setMeizu(tag.getMeizu() + 1);
            }
        }
        return tag;
    }
}

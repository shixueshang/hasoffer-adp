package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.FileUtil;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.TagStatistical;
import hasoffer.adp.core.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * Created by lihongde on 2016/12/2 15:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CoreConfiguration.class)
@Transactional(transactionManager = "txManager")
public class TestTag {

    @Resource
    TagService tagService;

    @Test
    public void test() {
        List<Map<String, String>> list = new ArrayList<>();
        String path = "F:\\work\\hasoffer\\bb";
        File[] files = FileUtil.getFiles(path);
        for(File file : files){
            list.addAll(FileUtil.readFileByLines(file));
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

        System.out.println("size ： " + result.size());
        System.out.println("satrt insert ... " + new Date());
        tagService.truncate();
        /*for(TagStatistical t : result){
            tagService.insert(t);
        }*/
        tagService.batchInsert(result);
        System.out.println("insert end... " + new Date());

    }

    /**
     * 解析map{androidid=tag} 得到新的map中androidid不重复
     * @param mapData
     * @param tagMap
     */
    private static void paraseMap(Map<String, String> mapData,  Map<String, List<String>> tagMap){
        for(Map.Entry<String, String> entry : mapData.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            if(!tagMap.containsKey(key)){
                List<String> newList = new ArrayList<>();
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
            if(value.trim().toLowerCase().contains("xiaomi")){
                tag.setXiaomi(tag.getXiaomi() + 1);
            }else if(value.trim().toLowerCase().contains("lenovo")){
                tag.setLenovo(tag.getLenovo() + 1);
            }else if(value.trim().toLowerCase().contains("redmi")){
                tag.setRedmi(tag.getRedmi() + 1);
            }else if(value.trim().toLowerCase().contains("huawei")){
                tag.setHuawei(tag.getHuawei() + 1);
            }else if(value.trim().toLowerCase().contains("honor")){
                tag.setHonor(tag.getHonor() + 1);
            }else if(value.trim().toLowerCase().contains("samsung")){
                tag.setSamsung(tag.getSamsung() + 1);
            }else if(value.trim().toLowerCase().contains("meizu")){
                tag.setMeizu(tag.getMeizu() + 1);
            }
        }
        return tag;
    }
}

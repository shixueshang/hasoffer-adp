package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.TagStatistical;
import hasoffer.adp.core.service.EquipmentService;
import hasoffer.adp.core.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
}

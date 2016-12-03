package hasoffer.adp.core.core.test.db;


import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.enums.Platform;
import hasoffer.adp.core.enums.SettlementWay;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.service.MaterialService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2016/11/29 16:12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CoreConfiguration.class)
@Transactional(transactionManager = "txManager")
public class TestMaterial {

    @Resource
    MaterialService materialService;

    @Test
   public void find(){
        Material m = materialService.find(1);
        if(m != null){
            System.out.println("-------" + m.getTitle());
        }
   }

    @Test
    public void insert(){
        Material m = new Material();
        m.setTitle("title");
        m.setPutPlatform(Platform.Android);
        m.setSettlementWay(SettlementWay.CPC);
        materialService.insert(m);
    }

    @Test
    public void findPage(){
        Page<Material> page = materialService.findPage(1, 10);
        System.out.println(page.getTotal());
        for(Material m : page.getItems()){
            System.out.println("-----"+m.getTitle());
        }
    }

    @Test
    public void update(){
        Material m = materialService.find(5);
        m.setSubTitle("subtitle2");
        materialService.update(m);
    }


}

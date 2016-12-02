package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.enums.AndroidVersion;
import hasoffer.adp.core.enums.Platform;
import hasoffer.adp.core.enums.SettlementWay;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.service.EquipmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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

    @Test
    public void insert(){
        Equipment e = new Equipment();
        e.setAndroidId(UUID.randomUUID().toString());
        e.setTags("tag1,tag2");
        e.setCreateTime(new Date());
        equipmentService.insert(e);
    }
}

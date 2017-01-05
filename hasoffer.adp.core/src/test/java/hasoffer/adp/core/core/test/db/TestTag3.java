package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Device;
import hasoffer.adp.core.models.po.Equipment;
import hasoffer.adp.core.service.EquipmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2017/1/3 16:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestTag3 {

    @Resource
    EquipmentService equipmentService;

    @Test
    public void findDevice() {
        List<Device> list = equipmentService.findDevice();

        List<Equipment> equipments = new ArrayList<>();
        for (Device d : list) {
            String tag = "";
            String deviceName = d.getDeviceName();
            if (deviceName.equals("Xiaomi Redmi Note 3 kenzo")) {
                tag = "redmi_note3";
            } else if (deviceName.equals("samsung SM-J200G j2ltedd")) {
                tag = "samsung_SM_J200G";
            } else if (deviceName.equals("samsung SM-J700F j7eltexx")) {
                tag = "samsung_SM_J700F";
            } else if (deviceName.equals("LENOVO Lenovo A6000 Kraft-A6000")) {
                tag = "lenovo_A6000";
            } else if (deviceName.equals("Xiaomi 2014818 2014818")) {
                tag = "redmi2";
            } else if (deviceName.equals("LENOVO Lenovo K50a40 aio_otfp")) {
                tag = "lenovo_K50a40";
            } else if (deviceName.equals("samsung SM-G7102 ms013gxx")) {
                tag = "samsung_SM_G7102";
            }

            Equipment e = new Equipment(d.getDeviceId(), tag);
            equipments.add(e);

        }

        System.out.println("total : " + equipments.size());
        System.out.println("start load data......." + new Date());
        equipmentService.batchInsert(equipments);
        System.out.println("load data success............." + new Date());
    }
}

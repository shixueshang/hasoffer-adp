package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Tag2;
import hasoffer.adp.core.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2017/1/10 18:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestTag4 {

    @Resource
    TagService tagService;

    @Test
    public void test() {
        List<Tag2> list = new ArrayList<>();
        String path = "F:\\work\\hasoffer\\phone.txt";
        File file = new File(path);
        BufferedReader reader = null;
        int count = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                String tag = tempString.split("\\t")[1];
                if (tag == null) {
                    continue;
                }
                String dd = tempString.trim().split("\\t")[0];
                if (StringUtils.isEmpty(dd)) {
                    continue;
                }
                int num = Integer.parseInt(dd.split(" ")[0]);
                String aid = dd.split(" ")[1];

                Tag2 t = new Tag2();
                t.setAid(aid);
                if (tag.equals("OnePlus_3T")) {
                    t.setOnePlus_3T(num);
                } else if (tag.equals("Moto_G_Plus,_4th_Gen")) {
                    t.setMoto_G_Plus_4th_Gen(num);
                } else if (tag.equals("Lenovo_Vibe_K5_Note")) {
                    t.setLenovo_Vibe_K5_Note(num);
                } else if (tag.equals("LeEco_Le_1s_Eco")) {
                    t.setLeEco_Le_1s_Eco(num);
                } else if (tag.equals("Moto_M")) {
                    t.setMoto_M(num);
                } else if (tag.equals("Lenovo_Phab_2")) {
                    t.setLenovo_Phab_2(num);
                } else if (tag.equals("Panasonic_Eluga_Note")) {
                    t.setPanasonic_Eluga_Note(num);
                } else if (tag.equals("SAMSUNG_Galaxy_On8")) {
                    t.setSAMSUNG_Galaxy_On8(num);
                } else if (tag.equals("SAMSUNG_Galaxy_On_Nxt")) {
                    t.setSAMSUNG_Galaxy_On_Nxt(num);
                } else if (tag.equals("Yu_Yureka_Plus")) {
                    t.setYu_Yureka_Plus(num);
                }

                list.add(t);

                line++;
            }
            reader.close();

            System.out.println("size ： " + list.size());
            System.out.println("satrt insert ... " + new Date());
            tagService.truncate2();

            tagService.batchInsertTag2(list);
            System.out.println("insert end... " + new Date());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}

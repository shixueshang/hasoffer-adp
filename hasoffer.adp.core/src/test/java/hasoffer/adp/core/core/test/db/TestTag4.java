package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Tag2;
import hasoffer.adp.core.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        String path = "F:\\work\\hasoffer\\0111\\lenovo.txt";
        File file = new File(path);
        BufferedReader reader = null;
        int count = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {

                int num = Integer.parseInt(tempString.split(" ")[1]);
                String aid = tempString.split(" ")[2];

                Tag2 t = new Tag2();
                t.setAid(aid);
                t.setLenovo_Vibe_K5_Note(num);

                list.add(t);

                line++;
            }
            reader.close();

            System.out.println("size ： " + list.size());
            System.out.println("satrt insert ... " + new Date());
            //tagService.truncate2();

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

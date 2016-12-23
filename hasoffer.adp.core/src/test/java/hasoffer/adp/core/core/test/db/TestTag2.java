package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.FileUtil;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.Tag;
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
import java.util.List;

/**
 * Created by lihongde on 2016/12/22 18:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestTag2 {

    @Resource
    TagService tagService;


    @Test
    public void test() {

        String path = "F:\\work\\hasoffer\\device_tags_201612";
        File f = new File(path);
        File[] flist = f.listFiles();

        for (File fd : flist) {
            List<Tag> list = new ArrayList<>();
            String name = fd.getName();

            String ymd = name.split("_")[2];

            File[] files = FileUtil.getFiles(path + "\\" + name);
            for (File file : files) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    int line = 1;
                    while ((tempString = reader.readLine()) != null) {
                        Tag t = new Tag();
                        t.setYmd(ymd);
                        tempString = tempString.substring(1, tempString.length() - 1);
                        String aid = tempString.split(",")[0];
                        t.setAid(aid);

                        String tags = tempString.split(",")[1];

                        String[] tagArr = tags.split("\\t");

                        t.setSamsung(Integer.parseInt(tagArr[1]));
                        t.setXiaomi(Integer.parseInt(tagArr[2]));
                        t.setRedmi(Integer.parseInt(tagArr[3]));
                        t.setMoto(Integer.parseInt(tagArr[4]));
                        t.setLeeco(Integer.parseInt(tagArr[5]));
                        t.setLenovo(Integer.parseInt(tagArr[6]));

                        list.add(t);
                        line++;
                    }
                    reader.close();
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

            tagService.batchInsertTag(list);

        }
    }

}

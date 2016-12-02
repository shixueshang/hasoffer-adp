package hasoffer.adp.base.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/2 15:43
 */
public class FileUtil {

    /**
     * 获得某个路径下的所有文件
     * @param filePath
     * @return
     */
    public static File[] getFiles(String filePath){
        File root = new File(filePath);
        return  root.listFiles();
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static Map<String, String> readFileByLines(File file) {
        Map<String, String> map = new ConcurrentHashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                map.put(tempString.split("\\t")[0].trim(), tempString.split("\\t")[1].trim());
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
        return map;
    }

}

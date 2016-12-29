package hasoffer.adp.base.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static List<Map<String, String>> readFileByLines(File file) {
        List<Map<String, String>> fileData = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                Map<String, String> fileMap = new ConcurrentHashMap<>();
                fileMap.put(tempString.split("\\t")[0].trim(), tempString.split("\\t")[1].trim());
                fileData.add(fileMap);
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
        return fileData;
    }

    public static String getMacAddress() {
        String mac = "";
        try {
            Process p = new ProcessBuilder("ifconfig").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                Pattern pat = Pattern.compile("\\b\\w+:\\w+:\\w+:\\w+:\\w+:\\w+\\b");
                Matcher mat = pat.matcher(line);
                if (mat.find()) {
                    mac = mat.group(0);
                }
            }
            br.close();
        } catch (IOException e) {
        }
        return mac;
    }


    public static <E> E getRandomElement(Set<E> set) {
        int rn = ThreadLocalRandom.current().nextInt(set.size());
        int i = 0;
        for (E e : set) {
            if (i == rn) {
                return e;
            }
            i++;
        }
        return null;
    }

}

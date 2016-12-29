package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.FileUtil;
import hasoffer.adp.base.utils.HttpPostGet;

import java.util.Set;

/**
 * Created by lihongde on 2016/12/27 16:53
 */
public class TestReqThread2 implements Runnable {


    private Set<String> keys;

    public TestReqThread2(Set<String> keys) {
        this.keys = keys;
    }


    @Override
    public void run() {
        final String url = "http://ad.hasoffer.cn/ym/getAd?imgw=100&imgh=1000&aid=%s";

        for (int i = 0; i < 10; i++) {

            String k = FileUtil.getRandomElement(keys);

            String real_url = String.format(url, k);

            HttpPostGet h = new HttpPostGet();
            try {
                String re = h.sendGet(real_url);
                int code = h.getResponseCode();

                System.out.println(String.format("response[%d] : %s", code, re));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

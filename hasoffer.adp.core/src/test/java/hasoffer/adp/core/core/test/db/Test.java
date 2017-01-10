package hasoffer.adp.core.core.test.db;


import hasoffer.site.helper.FlipkartHelper;

/**
 * Created by lihongde on 2016/12/22 20:08
 */
public class Test {

    public static void main(String[] args) {
        String s = "      9 2c7c5299d15808a\tSAMSUNG_Galaxy_On8";

        System.out.println(s.trim());
        System.out.println(s.trim().split("\\t")[1]);


        String dd = s.trim().split("\\t")[0];

        System.out.println(dd.split(" ")[0]);
        System.out.println(dd.split(" ")[1]);

    }

    @org.junit.Test
    public void t2() {
        String u = "http://www.amazon.in/iPaky-Luxury-Quality-Ultra-Thin-Silicon/dp/B01AJXSW5C/ref=sr_1_1?s=electronics&ie=UTF8&qid=1484038368&sr=1-1";
        String url = FlipkartHelper.getUrlWithAff(u, new String[]{"HASAD_YM", "779eb68b01472b6c"});

        System.out.println(url);
    }
}

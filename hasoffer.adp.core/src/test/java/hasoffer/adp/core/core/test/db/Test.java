package hasoffer.adp.core.core.test.db;


import hasoffer.site.helper.FlipkartHelper;

/**
 * Created by lihongde on 2016/12/22 20:08
 */
public class Test {

    public static void main(String[] args) {
        String ds = " 96 5121c51d95e3537e";

        //String num = ds.split("\\t")[0];
        String[] aid = ds.split(" ");

        System.out.println(aid[1]);
        System.out.println(aid[2]);
    }

    @org.junit.Test
    public void t2() {
        String u = "https://www.flipkart.com/ipaky-back-cover-xiaomi-redmi-note-3/p/itmeg6eakadfhfzb?pid=ACCEG6EAGFFYZSKR";
        String url = FlipkartHelper.getUrlWithAff(u, new String[]{"HASAD_YM", "779eb68b01472b6c"});

        System.out.println(url);
    }
}

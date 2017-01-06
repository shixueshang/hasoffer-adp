package hasoffer.adp.core.core.test.db;


import hasoffer.base.model.HttpResponseModel;
import hasoffer.base.utils.http.HttpUtils;

/**
 * Created by lihongde on 2016/12/22 20:08
 */
public class Test {

    public static void t1(String[] args) {
        String str = "\t3\t0\t0\t0\t0\t0";

        String[] a = str.split("\\t");

        System.out.println(a.length);
        for (String s : a) {
            System.out.println(s);
        }


    }

    @org.junit.Test
    public void t2() {
        String url = "https://www.flipkart.com/prepare-data-interpretation-cat/p/itmehf6pzpzw69rg?pid=9789352602254&srno=b_1_2&otracker=hp_omu_Deals%20of%20the%20Day_2_Min.%2055%25%20Off_b5967c00-8791-4b50-9589-51b8b39af5ad_0&lid=LSTBOK9789352602254BFSX1Y";
        HttpResponseModel rps = HttpUtils.get(url, null);

        System.out.println(rps.getBodyString());
    }
}

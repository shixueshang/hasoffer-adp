package hasoffer.adp.core.core.test.db;


/**
 * Created by lihongde on 2016/12/22 20:08
 */
public class Test {

    public static void main(String[] args) {
        String str = "\t3\t0\t0\t0\t0\t0";

        String[] a = str.split("\\t");

        System.out.println(a.length);
        for (String s : a) {
            System.out.println(s);
        }


    }
}

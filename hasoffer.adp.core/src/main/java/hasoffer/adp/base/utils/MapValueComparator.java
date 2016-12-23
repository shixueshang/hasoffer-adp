package hasoffer.adp.base.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map value比较大小排序
 * Created by lihongde on 2016/12/23 11:32
 */
public class MapValueComparator implements Comparator<String> {

    Map<String, Integer> base;

    public MapValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0)
                    return 1;
                else
                    return compare;

            }
        };

        Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;

    }

    public static void main(String[] args) {
        Map<String, Integer> t = new HashMap<>();
        t.put("xiaomi", 1);
        t.put("lev", 5);
        t.put("lee", 3);
        t.put("sam", 19);
        t.put("red", 2);

        MapValueComparator c = new MapValueComparator(t);
        Map<String, Integer> sorted_map = new TreeMap<String, Integer>(c);

        sorted_map.putAll(t);

        System.out.println(sorted_map);


    }

    @Override
    public int compare(String o1, String o2) {
        if (base.get(o1) > base.get(o2)) {
            return -1;
        } else {
            return 1;
        }
    }

}

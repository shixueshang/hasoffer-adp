package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.cache.EHCache;
import hasoffer.adp.core.configuration.CoreConfiguration;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lihongde on 2016/12/30 14:20
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestEhcache {

    @org.junit.Test
    public void putAndGet() {
        EHCache ehCache = EHCache.getInstance();

        ehCache.put("adpLocalCache", "name", "张三");

        Object n = ehCache.get("adpLocalCache", "name");

        System.out.println(n);


    }

}

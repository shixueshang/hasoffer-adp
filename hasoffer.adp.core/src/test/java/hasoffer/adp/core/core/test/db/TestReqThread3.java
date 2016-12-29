package hasoffer.adp.core.core.test.db;

import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.data.redis.IRedisMapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lihongde on 2016/12/27 16:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestReqThread3 extends Thread {

    @Resource
    IRedisMapService redisMapService;

    @Test
    public void test() {

        Set<String> keys = redisMapService.getKeys(Constants.REDIS_MAP_KEY.AIDTAGMAP);

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            es.execute(new TestReqThread2(keys));
        }

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

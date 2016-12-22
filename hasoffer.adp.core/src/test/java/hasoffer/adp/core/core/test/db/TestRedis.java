package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.data.redis.IRedisMapService;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by lihongde on 2016/12/22 12:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CoreConfiguration.class)
public class TestRedis {

    @Resource
    IRedisMapService redisMapService;

    public void testPutAid() {

    }
}

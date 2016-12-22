package hasoffer.adp.core.configuration.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lihongde on 2016/12/22 12:26
 */
@Configuration
@EnableCaching
@PropertySource("classpath:/redis.properties")
@ComponentScan("hasoffer.data.redis")
public class RedisConfigration {

    @Autowired
    Environment env;

    @Bean("redisTemplate")
    public RedisTemplate getRedisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();

        return redisTemplate;
    }

    @Bean("jedisConnectionFactory")
    public JedisConnectionFactory connectionFactory(JedisPoolConfig poolConfig) {
        JedisConnectionFactory conn = new JedisConnectionFactory();
        conn.setUsePool(Boolean.getBoolean(env.getProperty("redis.usePool")));
        conn.setHostName(env.getProperty("redis.hostname"));
        conn.setPort(Integer.parseInt(env.getProperty("redis.port")));
        conn.setTimeout(Integer.parseInt(env.getProperty("redis.timeout")));
        conn.setPassword(env.getProperty("redis.password"));
        conn.setPoolConfig(poolConfig);
        return conn;
    }

    @Bean("jedisPoolConfig")
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.parseInt(env.getProperty("redis.pool.maxIdle")));
        config.setMaxTotal(Integer.parseInt(env.getProperty("redis.pool.maxActive")));
        config.setMaxWaitMillis(Long.parseLong(env.getProperty("redis.pool.maxWait")));
        config.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("redis.pool.testOnBorrow")));
        return config;
    }


}

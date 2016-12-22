package hasoffer.adp.core.configuration.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lihongde on 2016/12/22 12:26
 */
@Configuration
@EnableCaching
@PropertySource("classpath:/redis.properties")
public class RedisConfigration {

    @Autowired
    Environment env;

    @Bean("jedisConnectionFactory")
    public JedisConnectionFactory connectionFactory(JedisPoolConfig poolConfig) {
        JedisConnectionFactory conn = new JedisConnectionFactory(poolConfig);
        conn.setHostName(env.getProperty("redis.host"));
        conn.setPort(Integer.parseInt(env.getProperty("redis.port")));
        conn.setDatabase(Integer.parseInt(env.getProperty("redis.dbIndex")));
        conn.setTimeout(Integer.parseInt(env.getProperty("redis.timeout")));
        conn.setPoolConfig(poolConfig);
        return conn;
    }

    @Bean("poolConfig")
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.parseInt(env.getProperty("redis.maxIdle")));
        config.setMaxTotal(Integer.parseInt(env.getProperty("redis.maxActive")));
        config.setMaxWaitMillis(Long.parseLong(env.getProperty("redis.maxWait")));
        config.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("redis.testOnBorrow")));
        return config;
    }


}

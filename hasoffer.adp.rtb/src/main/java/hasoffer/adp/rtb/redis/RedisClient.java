package hasoffer.adp.rtb.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RedisClient {

    RedisTemplate redisTemplate;

    public boolean expire(final String key, final long seconds) {
        return (Boolean) redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.expire(key.getBytes(), seconds);
            return true;
        });
    }

    public String mapGet(final String mName, final String key) {
        return (String) redisTemplate.execute((RedisCallback) redisConnection -> {
            byte[] vs = redisConnection.hGet(mName.getBytes(), key.getBytes());
            if (vs == null) {
                return null;
            }
            return new String(vs);
        });
    }

    public Map<String, String> mapGetAll(final String mName) {
        Map<String, String> map = new HashMap<String, String>();
        Map<byte[], byte[]> mapBytes = (Map<byte[], byte[]>) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hGetAll(mName.getBytes());
            }
        });

        if (mapBytes != null && mapBytes.size() > 0) {
            for (Map.Entry<byte[], byte[]> kv : mapBytes.entrySet()) {
                map.put(new String(kv.getKey()), new String(kv.getValue()));
            }
        }

        return map;
    }

    public boolean mapPut(final String mName, final String key, final String value) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.hSet(mName.getBytes(), key.getBytes(), value.getBytes());
                return true;
            }
        });
    }

    public String get(final String key, final long seconds) {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] values = redisConnection.get(key.getBytes());
                if (values == null) {
                    return null;
                }

                if (seconds > 0) {
                    redisConnection.expire(key.getBytes(), seconds);
                }

                return new String(values);
            }
        });
    }

    public boolean add(final String key, final String value, final long seconds) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(key.getBytes(), value.getBytes());

                if (seconds > 0) {
                    redisConnection.expire(key.getBytes(), seconds);
                }

                return true;
            }
        });
    }

    public List get(final Class<List> clazz, final String key, final long seconds) {
        String objJson = get(key, seconds);

        if (StringUtils.isEmpty(objJson)) {
            return null;
        }

            return JSON.parseArray(objJson, (Class) clazz);

    }

    public boolean add(String key, final Object t, long seconds) {
        try {
            String value = JSON.toJSON(t).toString();
            add(key, value, seconds);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void del(final String key) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(key.getBytes());
                return null;
            }
        });
    }

    public boolean exists(final String key) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.exists(key.getBytes());
            }
        });
    }

    public boolean update(final String key, Object t) {
        return false;
    }
}

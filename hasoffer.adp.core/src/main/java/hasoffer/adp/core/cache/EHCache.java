package hasoffer.adp.core.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


/**
 * Created by lihongde on 2016/12/30 11:59
 */
public class EHCache {

    private static EHCache cache;
    private CacheManager cacheManager = CacheManager.getInstance();

    public static EHCache getInstance() {
        if (cache == null) {
            cache = new EHCache();
        }
        return cache;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public Object get(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Cache get(String cacheName) {
        return cacheManager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.remove(key);
    }

}

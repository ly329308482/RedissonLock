package com.wcj.redisson.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chengjie.wang on 2017/9/30.
 */
public class RedissonJCacheManager<K, V> {

    private Map<String ,Cache<K,V>> cacheMap = new ConcurrentHashMap<String, Cache<K, V>>();

    private URI configUri = null;

    private Logger logger = LoggerFactory.getLogger(RedissonJCacheManager.class);

    public Cache<K,V> getCache(String cacheName, Duration duration) {
        Cache<K,V> cache = cacheMap.get(cacheName);
        if(null == cache){
            URL url= this.getClass().getClassLoader().getResource("jcache/redisson-clusterServerConfig-jcache.json");
            try {
                configUri = url.toURI();
            } catch (URISyntaxException e) {
                logger.error("URISyntaxException :"+e);
            }
            MutableConfiguration<K,V> mutableConfiguration = new MutableConfiguration<K, V>()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(duration))
                    .setStatisticsEnabled(false);
            CachingProvider cachingProvider = Caching.getCachingProvider();
            cache = cachingProvider.getCacheManager(configUri, null)
                    .createCache(cacheName, mutableConfiguration);
            cacheMap.put(cacheName,cache);
        }
        return cache;
    }
}

package com.wcj.redisson.lock.manager;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * redissonClient加载器，加载redissonClient实例
 *
 * Created by chengjie.wang on 2017/9/26.
 */
@Component
public class RedissionClientManager implements InitializingBean {

    @Autowired
    private Environment env;

    private static RedissonClient redisson;

    private Config config;

    private static final String REDISSION_ClIENT_NAME = "SHIT_IS_PERFECT";

    private static final Integer POOL_SIZE = 20;

    private static final Integer IDLE_SIZE = 15;

    private static final Integer TIME_OUT = 10000;

    private static final Logger logger = LoggerFactory.getLogger(RedissionClientManager.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        String redis_address = env.getProperty("redis.address");
        config = new Config();
        config.useSingleServer().setAddress(redis_address)
                .setConnectionPoolSize(POOL_SIZE)
                .setClientName(REDISSION_ClIENT_NAME)
                .setConnectionMinimumIdleSize(IDLE_SIZE)
                .setConnectTimeout(TIME_OUT);
        redisson = Redisson.create(config);
    }

    public static RedissonClient getRedissionClient(){
        Assert.notNull(redisson,"Redission init instance is fail");
        if(logger.isInfoEnabled()){
            logger.info("Redission init instance is success !"+redisson);
        }
        return redisson;
    }

}

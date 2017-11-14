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

import static com.wcj.redisson.lock.manager.RedissionClientManager.ConnectionMode.standalone;

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
        String redis_address_master = env.getProperty("redis.address.master");
        String redis_address_slave = env.getProperty("redis.address.slave");
        Integer redis_connection_mode = Integer.parseInt(env.getProperty("redis.connectionMode"));
        String redis_address_cluster = env.getProperty("redis.address.cluster");
        config = new Config();
        switch (redis_connection_mode){
//            standalone 单机
            case 1:
                config.useSingleServer().setAddress(redis_address_master)
                    .setConnectionPoolSize(POOL_SIZE)
                    .setClientName(REDISSION_ClIENT_NAME)
                    .setConnectionMinimumIdleSize(IDLE_SIZE)
                    .setConnectTimeout(TIME_OUT);
                break;
//            master_slave 主从
            case 2:
                config.useMasterSlaveServers().setClientName(REDISSION_ClIENT_NAME)
                        .setConnectTimeout(TIME_OUT)
                        .setMasterAddress(redis_address_master)
                        .addSlaveAddress(redis_address_slave)
                        .setMasterConnectionMinimumIdleSize(IDLE_SIZE)
                        .setMasterConnectionPoolSize(POOL_SIZE)
                        .setSlaveConnectionMinimumIdleSize(IDLE_SIZE)
                        .setSlaveConnectionPoolSize(POOL_SIZE);
                break;
            case 3:
                config.useClusterServers()
                        .addNodeAddress(redis_address_cluster.split(","))//这是用的集群server
                        .setScanInterval(2000) //设置集群状态扫描时间
                        .setMasterConnectionPoolSize(10000) //设置连接数
                        .setSlaveConnectionPoolSize(10000);
                break;
        }
        redisson = Redisson.create(config);
    }

    public static RedissonClient getRedissionClient(){
        Assert.notNull(redisson,"Redission init instance is fail");
        if(logger.isInfoEnabled()){
            logger.info("Redission init instance is success !"+redisson);
        }
        return redisson;
    }
    public static  void main(String[] args){
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("127.0.0.1:6379","172.16.9.113:6379");//这是用的集群server
//                        .setScanInterval(2000) //设置集群状态扫描时间
//                        .setMasterConnectionPoolSize(10000) //设置连接数
//                        .setSlaveConnectionPoolSize(10000);
        redisson = Redisson.create(config);
        System.out.println(redisson);
    }
      enum ConnectionMode{

        //单机，主从，哨兵，集群
        standalone(1),
        master_slave(2),
        sentinel(3),
        cluster(4);

        private Integer key ;
        ConnectionMode(int i) {
            this.key = i;
        }

        private Integer getKey(){
            return key;
        }
    }
}

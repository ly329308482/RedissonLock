package com.wcj.redisson.clusterinfo;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.web.model.po.AddClusterInfo;
import org.redisson.api.RedissonClient;
import org.redisson.cluster.ClusterConnectionManager;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

/**
 * Created by chengjie.wang on 2018/1/2.
 */
public class ClusterConnectionManagerFactory{


    public static class ClusterConnectionManagerFactoryHandler{
        private static ClusterConnectionManagerFactory getClusterConnectionManagerFactory =  new ClusterConnectionManagerFactory();
    }

    public static ClusterConnectionManagerFactory getInstance(){
        return  ClusterConnectionManagerFactoryHandler.getClusterConnectionManagerFactory;
    }

    private static RedissonClient redissionClientManager  = RedissionClientManager.getRedissionClient();

    private static  final String REDIS_PRE = "redis://";

    public  ClusterConnectionManager getClusterConnectionManager(AddClusterInfo addClusterInfo){
        String[] address_add = addAddressToConfig(addClusterInfo);
        Config configOld = redissionClientManager.getConfig();
        ClusterServersConfig clusterServersConfig_old = configOld.useClusterServers();
        clusterServersConfig_old.addNodeAddress(address_add);
        ClusterServersConfig clusterServersConfig = new ClusterServersConfig();
        clusterServersConfig.setClientName("shit")
            .setSlaveConnectionPoolSize(100)
                .setConnectTimeout(10000)
                .setScanInterval(10000);
        return new ClusterConnectionManager(clusterServersConfig,configOld);
    }

    /**
     * 将旧地址进行扩容
     * @param addClusterInfo
     * @return
     */
    private String[] addAddressToConfig(AddClusterInfo addClusterInfo) {
        String masterIp = addClusterInfo.getMasterIp();
        String masterPort = addClusterInfo.getMasterPort();
        String slaveIp = addClusterInfo.getSlaveIp();
        String slavePort = addClusterInfo.getSlavePort();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(REDIS_PRE).append(masterIp).append(":").append(masterPort).append(",");
        stringBuilder.append(REDIS_PRE).append(slaveIp).append(":").append(slavePort);
        return stringBuilder.toString().split(",");
    }
}

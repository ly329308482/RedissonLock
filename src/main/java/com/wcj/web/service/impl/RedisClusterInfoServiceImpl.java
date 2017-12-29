package com.wcj.web.service.impl;

import com.wcj.common.annotation.PropertiesMapping;
import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.web.model.vo.ClusterNodeInfo;
import com.wcj.web.service.RedisClusterInfoService;
import org.redisson.api.ClusterNode;
import org.redisson.api.ClusterNodesGroup;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Service
public class RedisClusterInfoServiceImpl implements RedisClusterInfoService {

    private RedissonClient redissonClient = RedissionClientManager.getRedissionClient();

    private Logger logger = LoggerFactory.getLogger(RedisClusterInfoServiceImpl.class);
    /**
     * 获取redis集群节点信息
     *
     * @return
     */
    @Override
    public  List<ClusterNodeInfo> getRedisClusterInfo() {
        List<ClusterNodeInfo> clusterNodeInfos = new ArrayList<>();
        try{
        ClusterNodesGroup clusterNodesGroup = redissonClient.getClusterNodesGroup();
        Collection<ClusterNode> nodes = clusterNodesGroup.getNodes();
        Iterator<ClusterNode> iterator = nodes.iterator();
        while(iterator.hasNext()){
            ClusterNode clusterNode =   iterator.next();
            packgetClusterNodeInfo(clusterNodeInfos,clusterNode);
        }
        }catch (Exception e){
            logger.error("获取集群节点异常",e);
        }
        return clusterNodeInfos;
    }

    /**
     * 将集群信息封装到ClusterNodeInfo
     * @param clusterNodeInfos
     * @param clusterNode
     */
    private void packgetClusterNodeInfo(List<ClusterNodeInfo> clusterNodeInfos, ClusterNode clusterNode) {
        ClusterNodeInfo clusterNodeInfo = new ClusterNodeInfo();
        Map<String, String> clusterNodeMap = clusterNode.clusterInfo();
        Field[] fields = clusterNodeInfo.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true); //设置些属性是可以访问的
            if(field.isAnnotationPresent(PropertiesMapping.class)){
                PropertiesMapping pm = field.getAnnotation(PropertiesMapping.class);
                String mapingValue = pm.value();
                String value = clusterNodeMap.get(mapingValue);
                try {
                    field.set(clusterNodeInfo,value);
                } catch (IllegalAccessException e) {
                    logger.error("集群对象设值失败",e);
                }
            }
        }
        clusterNodeInfos.add(clusterNodeInfo);
    }
}

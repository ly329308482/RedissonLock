package com.wcj.redisson.nodeinfo;

import com.alibaba.fastjson.JSON;
import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.ClusterNode;
import org.redisson.api.ClusterNodesGroup;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by chengjie.wang on 2017/12/28.
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ClusterNodesGroupTest {

    @Test
    public void clusterNodesGroupTest(){
        RedissonClient redissonClient = RedissionClientManager.getRedissionClient();

        ClusterNodesGroup clusterNodesGroup = redissonClient.getClusterNodesGroup();
        Collection<ClusterNode> nodes = clusterNodesGroup.getNodes();
        Iterator<ClusterNode> iterator = nodes.iterator();
        while(iterator.hasNext()){
          ClusterNode clusterNode =   iterator.next();
          PrintUtil.printInfo(JSON.toJSONString(clusterNode.clusterInfo()));
          InetSocketAddress address = clusterNode.getAddr();
            String hostName = address.getHostName();
            int port = address.getPort();
            String addressString = address.getHostString();
            PrintUtil.printInfo(hostName);
            PrintUtil.printInfo(String.valueOf(port));
            PrintUtil.printInfo(addressString);
        }
    }
}

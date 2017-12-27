package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chengjie.wang on 2017/12/26.
 *
 * 7001 - 7004 为master-slave
 * 手动kill 掉7001 ，可从7004上拿到ownerDouble数据
 * 启动7001，主从关系为
 * 7004 -7001 master-slave
 * 集群高可用
 *
 * CLUSTER FAILOVER [FORCE|TAKEOVER] 在slave上执行，主从切换
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RDouble {

    @Test
    public void rDoubleaddgettest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RAtomicDouble rdouble = redissionClient.getAtomicDouble("ownerDouble");
        rdouble.set(12);
        double value = rdouble.get();
        PrintUtil.printInfo(String.valueOf(value));
    }

    @Test
    public void rDoublegettest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RAtomicDouble rdouble = redissionClient.getAtomicDouble("ownerDouble");
        double value = rdouble.get();
        PrintUtil.printInfo(String.valueOf(value));
    }
}

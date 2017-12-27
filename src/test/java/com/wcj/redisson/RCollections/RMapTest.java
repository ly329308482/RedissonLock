package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RAtomicDouble;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chengjie.wang on 2017/12/26.
 */@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RMapTest {

    @Test
    public void rMapPuttest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RMap<String,String> rMap = redissionClient.getMap("ownerMap");
        rMap.fastPut("heihhei","heihei");
    }

    @Test
    public void rMapgettest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RMap<String,String> rMap = redissionClient.getMap("ownerMap");
        String value =  rMap.get("hh");
        PrintUtil.printInfo(value);
    }
}

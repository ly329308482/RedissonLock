package com.wcj.redisson.jedis;

import com.alibaba.fastjson.JSON;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Created by chengjie.wang on 2017/12/28.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JedisTest {

    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void jedisTest(){
        Map<String,JedisPool> map = jedisCluster.getClusterNodes();
        PrintUtil.printInfo(JSON.toJSONString(map));
    }

}

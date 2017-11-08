package com.wcj.redisson.cache;

import com.wcj.emp.service.EmpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chengjie.wang on 2017/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedissonCacheTest {
    Logger logger = LoggerFactory.getLogger(RedissonCacheTest.class);
    @Autowired
    private EmpService empService;
    @Test
    public void jcacheTest(){
        String result = empService.queryEmp();
        logger.info("result:"+result);
    }
}

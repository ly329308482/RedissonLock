package com.wcj.redisson.cache;

import com.wcj.emp.service.EmpRWService;
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
    private EmpRWService empService;
    @Test
    public void jcacheTest(){
        for(int i = 0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result = empService.queryEmp();
                    logger.info("result:"+result);
                }
            }).start();
        }
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

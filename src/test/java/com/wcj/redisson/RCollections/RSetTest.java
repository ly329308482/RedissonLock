package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by chengjie.wang on 2017/12/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RSetTest {

    private boolean breakFlag;

    public boolean isBreakFlag() {
        return breakFlag;
    }

    public void setBreakFlag(boolean breakFlag) {
        this.breakFlag = breakFlag;
    }


    @Test
    public void RsetPuttest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RSet<String> rset = redissionClient.getSet("ownerSet");
        rset.add("1");
    }

    /**
     * RSet线程安全，适合分布式场景运用，
     * 如将取出500个pkid，在分布式环境下可以将这500个pkid丢入Rset中，方便分布式拿取和删减
     * 需要加分布式锁，防止重复拿取
     * 线程1 add "set1"
     * 线程2 add "set2"
     * 线程3 remove "set1 ,set2"
     */
    @Test
    public void RsetPutandRmtest(){
        CountDownLatch cdlmain = new CountDownLatch(1);
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RSet<String> rset = redissionClient.getSet("ownerSet");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdlmain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rset.add("set1");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdlmain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rset.add("set2");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //读set 并且remove
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdlmain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Iterator<String> iterator = rset.iterator();
                for(;;){
                    if(iterator.hasNext()) {
                        PrintUtil.printInfo("--------------------我是和谐的分割线-----------------------");
                        String setValue = iterator.next();
                        PrintUtil.printInfo("set值是："+setValue);
                        rset.remove(setValue);
                        PrintUtil.printInfo("breakFlag："+atomicInteger.get());
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdlmain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Iterator<String> iterator = rset.iterator();
                for(;;){
                    if(iterator.hasNext()) {
                        PrintUtil.printInfo("--------------------我是和谐的分割线-----------------------");
                        String setValue = iterator.next();
                        PrintUtil.printInfo("set值是："+setValue);
                        rset.remove(setValue);
                        PrintUtil.printInfo("breakFlag："+atomicInteger.get());
                    }
                }
            }
        }).start();
        cdlmain.countDown();
        await().forever().until(()-> rset.size() == 0);
    }


    public void RsetPutandGettest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RSet<String> rset = redissionClient.getSet("ownerSet");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                rset.add("set1");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                rset.add("set2");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //读set
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<String> iterator = rset.iterator();
                for(;;){
                    if(iterator.hasNext()) {
                        PrintUtil.printInfo("--------------------我是和谐的分割线-----------------------");
                        PrintUtil.printInfo("set值是："+iterator.next());
                        PrintUtil.printInfo("breakFlag："+atomicInteger.get());
                        if(atomicInteger.get() == 2){
                            setBreakFlag(true);
                        }
                    }
                }
            }
        }).start();
        await().forever().until(()-> isBreakFlag());
    }
    /**
     * hashset线程不安全
     */
    @Test
    public void Rsetgettest(){
        AtomicInteger atomicInteger = new AtomicInteger(0);
       Set set = new HashSet<>();
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                set.add("set1");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //写set
        new Thread(new Runnable() {
            @Override
            public void run() {
                set.add("set2");
                atomicInteger.incrementAndGet();
            }
        }).start();
        //读set
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<String> iterator = set.iterator();
                for(;;){
                    if(iterator.hasNext()) {
                        PrintUtil.printInfo("--------------------我是和谐的分割线-----------------------");
                        PrintUtil.printInfo("set值是："+iterator.next());
                        PrintUtil.printInfo("breakFlag："+atomicInteger.get());
                        if(atomicInteger.get() == 2){
                            setBreakFlag(true);
                        }
                    }
                }
            }
        }).start();
        await().forever().until(()-> isBreakFlag());
    }
}

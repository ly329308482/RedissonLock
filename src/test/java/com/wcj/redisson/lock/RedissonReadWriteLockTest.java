package com.wcj.redisson.lock;

import com.wcj.redisson.lock.util.DistributedReadAndWriteLock;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;

/**
 * 读写锁适用于读多写少的情况，可以实现更好的并发性
 * Created by chengjie.wang on 2017/12/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedissonReadWriteLockTest {

    /**
     * 读-写互斥，写锁没有释放，读锁就无法获取
     * 读-读不互斥
     * @throws InterruptedException
     */
    @Test
    public void readLockLeaseTime() throws InterruptedException {
        RLock wLock = DistributedReadAndWriteLock.wLock("rock me!!");
        //持续获取时间为1秒，持有时间10秒
        Assert.assertTrue(wLock.tryLock(1,10, TimeUnit.SECONDS));
        final AtomicInteger excuted = new AtomicInteger();
        //线程1，获取读锁
        Thread t1 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            excuted.incrementAndGet();
        });
        //线程2，获取读锁
        Thread t2 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            excuted.incrementAndGet();
        });
        t1.start();
        t2.start();
        //等待至少11秒，直到until返回true
        await().atMost(11, TimeUnit.SECONDS).until(() -> excuted.get() == 2);
    }

    /**
     * 读共享，资源共享，读锁不释放，不阻塞
     * excuted+2
     * @throws InterruptedException
     */
    @Test
    public void readLock() throws InterruptedException {
        final AtomicInteger excuted = new AtomicInteger();
        //线程1，获取读锁
        Thread t1 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t1.setName("我是1号");
        //线程2，获取读锁
        Thread t2 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t2.setName("我是2号");
        t1.start();
        t2.start();
        //等待至少2秒，直到until返回true
        await().atMost(5, TimeUnit.SECONDS).until(() -> excuted.get() == 2);
    }

    /**
     * 读写互斥
     * 读锁不释放，写锁拿不到，阻塞线程
     * excuted = 0
     * @throws InterruptedException
     */
    @Test
    public void readNoRealseAndWriteLock() throws InterruptedException {
        final AtomicInteger excuted = new AtomicInteger();
        //线程1，获取读锁
        Thread t1 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            RLock wLock = DistributedReadAndWriteLock.wLock("rock me!!");
            wLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t1.setName("我是1号");
        //线程2，获取读锁
        Thread t2 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            RLock wLock = DistributedReadAndWriteLock.wLock("rock me!!");
            wLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t2.setName("我是2号");
        t1.start();
        t2.start();
        //等待至少2秒，直到until返回true
        await().atMost(2, TimeUnit.SECONDS).until(() -> excuted.get() == 0);
    }

    /**
     *读锁都释放，线程1，线程2同时去竞争写锁，其中一个拿到锁，不释放，另外一个阻塞
     * excuted+1
     * @throws InterruptedException
     */
    @Test
    public void readRealseAndWriteLock() throws InterruptedException {
        final AtomicInteger excuted = new AtomicInteger();
        //线程1，获取读锁
        Thread t1 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            rLock.unlock();
            RLock wLock = DistributedReadAndWriteLock.wLock("rock me!!");
            wLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t1.setName("我是1号");
        //线程2，获取读锁
        Thread t2 = new Thread(() -> {
            RLock rLock = DistributedReadAndWriteLock.rLock("rock me!!");
            rLock.lock();
            rLock.unlock();
            RLock wLock = DistributedReadAndWriteLock.wLock("rock me!!");
            wLock.lock();
            excuted.incrementAndGet();
            PrintUtil.printInfo(Thread.currentThread().getName()+" do something");
        });
        t2.setName("我是2号");
        t1.start();
        t2.start();
        //等待至少2秒，直到until返回true
        await().atMost(2, TimeUnit.SECONDS).until(() -> excuted.get() == 1);
    }

}

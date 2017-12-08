package com.wcj.redisson.lock.worker;

import com.wcj.redisson.lock.constant.RedissonConstant;
import com.wcj.redisson.lock.util.DistributedRedissionLock;
import com.wcj.redisson.lock.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengjie.wang on 2017/9/29.
 */
public class Worker<T> extends BaseWorker implements Runnable  {

    private static Logger logger = LoggerFactory.getLogger(Worker.class);

    private  CountDownLatch countDownLatch;

    private T  t;

    private Long expireTime;

    private TimeUnit unit;

    //解锁标志
    private volatile boolean unLockFlag;

    private static String lockName = RedissonConstant.LOCK_PRE + "lockMyself";

    public Worker(CountDownLatch cdl ,T t,boolean unlockFlag,Long expireTime, TimeUnit unit){
        this.countDownLatch  = cdl;
        this.t = t;
        this.unLockFlag = unlockFlag;
        this.expireTime = expireTime;
        this.unit = unit;
    }

    public Worker(CountDownLatch cdl ,T t,boolean unlockFlag){
        this.countDownLatch  = cdl;
        this.t = t;
        this.unLockFlag = unlockFlag;
    }
    @Override
    public void run() {
        hasLockDone();
    }

    public   void hasLockDone() {
        try {
            //等待发令枪执行，线程阻塞
            countDownLatch.await();
            //加锁控制
            if(null == expireTime){
                DistributedRedissionLock.lock(lockName);
            }else{
                //设置锁持有
                DistributedRedissionLock.lock(lockName,expireTime,unit);
            }
            //打印加锁后状态
            PrintUtil.printInfo(Thread.currentThread()+ "加锁后状态====>" + DistributedRedissionLock.islocked(lockName));
            Assert.isTrue(DistributedRedissionLock.islocked(lockName),Thread.currentThread()+"锁失败");
            //业务操作
            operatorParam(t);
        } catch (Exception e) {
            logger.error("get lock exception..." + e);
        } finally {
            PrintUtil.printInfo(Thread.currentThread()+ "释放锁前状态====>" + DistributedRedissionLock.islocked(lockName));
            //解锁操作
//            Assert.isTrue(unLockFlag,"放弃释放锁，会造成死锁");
            if(unLockFlag){
                DistributedRedissionLock.unlock(lockName);
            }
//            System.out.println(Thread.currentThread()+"释放锁后的状态====>"+DistributedRedissionLock.islocked(lockName));
//            Assert.isTrue(!DistributedRedissionLock.islocked(lockName),"释放失败");
        }
    }
}

package com.wcj.redisson.lock.util;

import com.wcj.redisson.lock.constant.RedissonConstant;
import com.wcj.redisson.lock.manager.RedissionClientManager;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 * Created by chengjie.wang on 2017/9/26.
 */
public class DistributedRedissionLock{

    private static RedissonClient redissonClient = RedissionClientManager.getRedissionClient();

    private static Logger LOG = LoggerFactory.getLogger(DistributedRedissionLock.class);

    /**
     * 获取锁
     * @param lockkey
     * @return
     */
    public static boolean tryLock(String lockkey){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        return rlock.tryLock();
    }

    /**
     * 在指定时间内获取锁
     * @param lockkey
     * @param timeout
     * @param timeUnit
     * @return
     */
    public static boolean tryLock(String lockkey ,long timeout, TimeUnit timeUnit){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        boolean acquireflag = false;
        RLock rlock = redissonClient.getLock(lockName);
        try {
            acquireflag = rlock.tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            LOG.error("try acquire lock exception ..." +e );
        }
        return acquireflag;
    }

    /**
     * 加锁
     * @param lockkey
     */
    public static void lock(String lockkey){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        rlock.lock();
    }

    /**
     * 加锁，并设置锁时间
     * @param lockkey
     * @param timeout
     * @param timeUnit
     */
    public static void lock(String lockkey, long timeout, TimeUnit timeUnit){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        rlock.lock(timeout,timeUnit);
    }

    /**
     * 解锁
     * @param lockkey
     */
    public static void unlock(String lockkey){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        rlock.unlock();
    }

    /**
     * 强制解锁
     * @param lockkey
     */
    public static void focusunlock(String lockkey){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        rlock.forceUnlock();
    }

    /**
     * 是否释放成功
     * @param lockkey
     * @return
     */
    public static boolean islocked(String lockkey){
        String lockName = RedissonConstant.LOCK_PRE+lockkey;
        RLock rlock = redissonClient.getLock(lockName);
        return rlock.isLocked();
    }
}

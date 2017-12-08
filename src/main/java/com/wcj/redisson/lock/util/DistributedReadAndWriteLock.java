package com.wcj.redisson.lock.util;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

/**
 * 分布式读写锁
 * Created by chengjie.wang on 2017/12/5.
 */
public class DistributedReadAndWriteLock {

    private static RedissonClient redissonClient = RedissionClientManager.getRedissionClient();

    /**
     * 读锁
     * @param lockName
     */
    public static RLock rLock(String lockName){
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock(lockName);
        return rReadWriteLock.readLock();
    }

    /**
     * 写锁
     * @param lockName
     */
    public static RLock wLock(String lockName){
        RReadWriteLock rReadWriteLock = redissonClient.getReadWriteLock(lockName);
        return  rReadWriteLock.writeLock();
    }
}

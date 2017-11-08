package com.wcj.redisson.lock;

/**
 * Created by chengjie.wang on 2017/9/29.
 */
public abstract class BaseRedissonLockTest {

    //加锁测试，等待锁释放
    public abstract void lockTest() throws InterruptedException;

    //获取锁，立即返回结果 true为锁取到，false，为未获取到
    public abstract void tryLockTest() throws InterruptedException;

    //获取锁，锁持有时间
    public abstract void lockExpireTest() throws InterruptedException;

    //获取锁，锁获取时间范围
    public abstract void tryLockExpireTest() throws InterruptedException;

    //解锁
    public abstract void unlockTest();

    //强制解锁
    public abstract void focusUnlockTest();

    //死锁测试
    public abstract void deadLockTest() throws InterruptedException;


}

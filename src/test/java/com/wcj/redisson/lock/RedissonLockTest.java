package com.wcj.redisson.lock;

import com.wcj.redisson.lock.constant.RedissonConstant;
import com.wcj.redisson.lock.util.DistributedRedissionLock;
import com.wcj.redisson.lock.util.PrintUtil;
import com.wcj.redisson.lock.worker.Worker;
import com.wcj.redisson.lock.worker.WorkerTry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *分布式锁测试
 * Created by chengjie.wang on 2017/9/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedissonLockTest extends BaseRedissonLockTest {
    //并发数
    private static final int conNum = 10;
    //发令枪
    private static  CountDownLatch done = new CountDownLatch(1);

    private  static List increase = new ArrayList();

    @Test
    @Override
    public void lockTest() throws InterruptedException {
        //十个线程并发执行
        for( int i = 0; i<conNum ; i++){
            Thread thread = new Thread(new Worker(done,increase,true));
            thread.setName(i+"号线程");
            thread.start();
        }
        PrintUtil.printInfo("主线程结束，其他线程开始工作");
        //发令枪打枪，其他线程开始运行
        done.countDown();
        //主线程结束之后，类似jvm终止，finally代码块始终不执行
        // 所以需要加长主线程睡眠时间
        Thread.sleep(100000);
    }

    /**
     * 设置锁超时时间
     * @throws InterruptedException
     */
    @Override
    @Test
    public void lockExpireTest() throws InterruptedException {
        //十个线程并发执行
        for( int i = 0; i<conNum ; i++){
            //锁持有时间5秒钟
            Thread thread = new Thread(new Worker(done,increase,false,5L, TimeUnit.SECONDS));
            thread.setName(i+"号线程");
            thread.start();
        }
        PrintUtil.printInfo("主线程结束，其他线程开始工作");
        //发令枪打枪，其他线程开始运行
        done.countDown();
        //主线程结束之后，类似jvm终止，finally代码块始终不执行
        // 所以需要加长主线程睡眠时间
        Thread.sleep(30000);
    }

    /**
     * 十个线程并发去获取锁，只有一个能获取到并且正常执行，其他线程因为锁占用，立即返回未获取锁的异常结果
     * 在finally里执行解锁时，因为没有获取到锁，而会报异常
     *tryLock异步执行处理结果，而不会等待（非阻塞）
     * lock则会等待锁的释放(阻塞)
     * @throws InterruptedException
     */
    @Test
    @Override
    public void tryLockTest() throws InterruptedException {
        //十个线程并发执行
        for( int i = 0; i<conNum ; i++){
            Thread thread = new Thread(new WorkerTry(done,increase,true));
            thread.setName(i+"号线程");
            thread.start();
        }
        PrintUtil.printInfo("主线程结束，其他线程开始工作");
        //发令枪打枪，其他线程开始运行
        done.countDown();
        //主线程结束之后，类似jvm终止，finally代码块始终不执行
        // 所以需要加长主线程睡眠时间
        Thread.sleep(10000);
    }

    /**
     * 启动两个线程，线程1先执行，持锁时间为5秒钟，不做锁关闭操作。
     *线程2在十秒内尝试获取锁，如果能在十秒内获取到，则有效，反之无效
     * @throws InterruptedException
     */
    @Test
    @Override
    public void tryLockExpireTest() throws InterruptedException {
        //十个线程并发执行
        //锁持有时间为5秒
        Thread thread = new Thread(new Worker(done,increase,false,5L, TimeUnit.SECONDS));
        thread.setName("1号线程...trylockExpire");
        thread.start();
        Thread thread2 = new Thread(new WorkerTry(done, increase, true, 10L, TimeUnit.SECONDS));
        thread2.setName("2号线程...trylock");
        thread2.start();
        PrintUtil.printInfo("主线程结束，其他线程开始工作");
        //发令枪打枪，其他线程开始运行
        done.countDown();
        //主线程结束之后，类似jvm终止，finally代码块始终不执行
        // 所以需要加长主线程睡眠时间
        Thread.sleep(10000);
    }

    /**
     * 解锁
     */
    @Test
    @Override
    public void unlockTest() {
        String lockName = RedissonConstant.LOCK_PRE + "lockMyself";
        //上锁
        DistributedRedissionLock.lock(lockName);
        //上锁后状态
        PrintUtil.printInfo("上锁后锁状态==>"+DistributedRedissionLock.islocked(lockName));
        //解锁
        DistributedRedissionLock.unlock(lockName);
        //解锁后状态
        PrintUtil.printInfo("解锁后锁状态==>"+DistributedRedissionLock.islocked(lockName));
    }

    /**
     * 强制解锁
     */
    @Test
    @Override
    public void focusUnlockTest() {
        String lockName = RedissonConstant.LOCK_PRE + "lockMyself";
        //上锁
        DistributedRedissionLock.lock(lockName);
        //上锁后状态
        PrintUtil.printInfo("上锁后锁状态==>"+DistributedRedissionLock.islocked(lockName));
        //解锁
        DistributedRedissionLock.focusunlock(lockName);
        //解锁后状态
        PrintUtil.printInfo("解锁后锁状态==>"+DistributedRedissionLock.islocked(lockName));
    }

    /**
     * 死锁测试，加锁而不释放
     * @throws InterruptedException
     */
    @Test
    @Override
    public void deadLockTest() throws InterruptedException {
        //十个线程并发执行
        for( int i = 0; i<conNum ; i++){
            Thread thread = new Thread(new Worker(done,increase,false));
            thread.setName(i+"号线程");
            thread.start();
        }
        PrintUtil.printInfo("主线程结束，其他线程开始工作");
        //发令枪打枪，其他线程开始运行
        done.countDown();
        //主线程结束之后，类似jvm终止，finally代码块始终不执行
        // 所以需要加长主线程睡眠时间
        Thread.sleep(30000);
    }
}
/**
 *
 * 发票号并发处理方式
 * //判断锁是不是占用状态
 * 1.if(DistributedRedissionLock.islocked(lockName)){
 *     throw message ("发票正在打印")
 * }else{
 *      //设置锁持有
 *   DistributedRedissionLock.lock(lockName,expireTime,unit);
 *   //执行业务方法
 * }
 * finally{
 *     关闭锁
 *     DistributedRedissionLock.unlock(lockName);
 * }
 */

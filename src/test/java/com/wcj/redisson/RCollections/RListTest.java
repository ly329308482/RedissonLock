package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RFuture;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.awaitility.Awaitility.await;

/**
 * Created by chengjie.wang on 2017/12/25.
 * master-slave :
 * 09665730c44ddf2de07d50f756897405306af366 127.0.0.1:7001 myself,master - 0 0 2 connected 5461-10922
 36124118a825e750afa73414b084db2f917bb9ed 127.0.0.1:7005 slave 1cb9162358b9fcb5e46790aa5b344fc4d094398b 0 1514259234514 6 connected
 4c96f6e5341d75299b83b3df6b47c9b9b2cf5d56 127.0.0.1:7000 master - 0 1514259238829 1 connected 0-5460
 bfa437e31c6fb10534d6c3733d592983a1f7d2cd 127.0.0.1:7003 slave 4c96f6e5341d75299b83b3df6b47c9b9b2cf5d56 0 1514259235764 4 connected
 b199a27752acac72934ee7cf47a2008c405bb218 127.0.0.1:7004 slave 09665730c44ddf2de07d50f756897405306af366 0 1514259237808 5 connected
 1cb9162358b9fcb5e46790aa5b344fc4d094398b 127.0.0.1:7002 master - 0 1514259236811 3 connected 10923-16383
 *
 *
 * $ redis-cli -h IP -p port
 * D:\offen\redis\Redis-x64-3.0.504>redis-cli -h localhost -p 7000
 localhost:7000>
 localhost:7000> llen "ownerList"
 (integer) 7
 D:\offen\redis\Redis-x64-3.0.504>redis-cli -h localhost -p 7000
 localhost:7000> keys *
 1) "ownerList"
 localhost:7000> exit

 D:\offen\redis\Redis-x64-3.0.504>redis-cli -h localhost -p 7003
 localhost:7003> keys *
 1) "ownerList"
 localhost:7003>

 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RListTest {

    @Test
    public void rListAddTest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RList<String> rList = redissionClient.getList("ownerList");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        CountDownLatch cdl = new CountDownLatch(1);
        CountDownLatch cdla = new CountDownLatch(3);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rList.add("呵呵哒");
                atomicInteger.incrementAndGet();
                cdla.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rList.add("么么哒");
                atomicInteger.incrementAndGet();
                cdla.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rList.remove("呵呵哒");
                atomicInteger.incrementAndGet();
                cdla.countDown();
            }
        }).start();
        cdl.countDown();
        try {
            cdla.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PrintUtil.printInfo(atomicInteger.toString());
        PrintUtil.printInfo(String.valueOf(rList.size()));
        for(String str : rList){
            PrintUtil.printInfo(str);
        }
        await().atMost(5, TimeUnit.SECONDS).until(() -> atomicInteger.toString().equals("3"));
    }

    @Test
    public void rListgetTest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RList<String> rList = redissionClient.getList("ownerList");
        for(String str : rList.readAll()){
            PrintUtil.printInfo(str);
        }
    }

    @Test
    public void rListRmTest(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RList<String> rList = redissionClient.getList("ownerList");
        RFuture future = rList.removeAsync(1);
        try {
            PrintUtil.printInfo(future.get().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

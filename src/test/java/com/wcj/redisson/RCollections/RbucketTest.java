package com.wcj.redisson.RCollections;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.redisson.lock.util.PrintUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by chengjie.wang on 2017/12/27.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RbucketTest {
    @Test
    public void bucketPutTest() {
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBucket rBucket = redissionClient.getBucket("ownerBucket");
        rBucket.set("aaa");
        assertThat(rBucket.get().equals("aaa"));
    }
    @Test
    public void bucketgetTest() {
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBucket rBucket = redissionClient.getBucket("ownerBucket");
        PrintUtil.printInfo(rBucket.get().toString());
        PrintUtil.printInfo(String.valueOf(rBucket.size()));
//        assertThat(rBucket.size()).isEqualTo(1);
//        assertThat(rBucket.get()).isEqualTo("aaa");
    }

    /**
     * Multi-key。Redis cluster对多key操作有限，要求命令中所有的key都属于一个slot，才可以被执行。
     * 客户端可以对multi-key命令进行拆分，再发给redis。
     * 另外一个局限是，在slot迁移过程中，
     * multi-key命令特别容易报错(CROSSSLOT Keys in request don’t hash to the same slot)。
     * 建议不用multi-key命令。
     * org.redisson.client.RedisException: CROSSSLOT Keys in request don't hash to the same slot. channel: [id: 0xb700e120,
     * L:/127.0.0.1:22040 - R:/127.0.0.1:7005] command: (MGET), params: [bucket1, bucket2]
     */
    @Test
    public void testMulGet(){
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBucket<String> bucket1 = redissionClient.getBucket("bucket1");
        bucket1.set("value1");
        RBucket<String> bucket2 = redissionClient.getBucket("bucket2");
        bucket2.set("value2");

//        Map<String ,String> getBuckets = redissionClient.getBuckets().get("bucket1","bucket2","ownerBucket");

        Map<String ,String> getBuckets = redissionClient.getBuckets().get("bucket1","bucket2");
        Map<String ,String> expected = new HashMap<String, String>();
        expected.put("bucket1","value1");
        expected.put("bucket2","value2");
//        expected.put("ownerBucket","aaa");
        assertThat(getBuckets).isEqualTo(expected);

    }

    @Test
    public void testexpireGet() throws InterruptedException {
        RedissonClient redissionClient = RedissionClientManager.getRedissionClient();
        RBucket<String> bucket1 = redissionClient.getBucket("bucketTime");
        bucket1.trySet("bucketTime",5L, TimeUnit.SECONDS);
        String getBuckets = bucket1.get();
//        expected.put("ownerBucket","aaa");
        assertThat(getBuckets).isEqualTo("bucketTime");
        Thread.sleep(6000l);
        assertThat(bucket1.get()).isEqualTo(null);
//        await().atMost(5L,TimeUnit.SECONDS).until(()-> bucket1.get() == null);
    }
}

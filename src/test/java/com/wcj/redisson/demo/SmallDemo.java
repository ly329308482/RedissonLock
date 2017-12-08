package com.wcj.redisson.demo;

import com.wcj.redisson.lock.util.PrintUtil;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chengjie.wang on 2017/12/6.
 */
public class SmallDemo {
    public static  void main(String[] args) {
        //随机数
//        Random r = new SecureRandom();
//        boolean nextBoolean = r.nextBoolean();
//        PrintUtil.printInfo(String.valueOf(nextBoolean));

        //原子封装类
//        final AtomicInteger integer = new AtomicInteger();
//        integer.incrementAndGet();
//        PrintUtil.printInfo(integer.toString());
//
//        integer.incrementAndGet();
//        PrintUtil.printInfo(integer.toString());
        TreeSet set  = new TreeSet();
        System.out.println(set.add(1));
        System.out.println(set.add(2));
        System.out.println(set.add(1));
        List list = new ArrayList(set);
        for(int i = 0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}

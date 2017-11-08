package com.wcj.redisson.lock.util;

/**
 * 打印类
 * Created by chengjie.wang on 2017/9/29.
 */
public class PrintUtil {

    public static void printInfo(String arg){
        System.out.println(DateUtil.currentTime("yyyy-MM-dd HH:mm:ss sss ")+arg);
    }
}

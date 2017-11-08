package com.wcj.redisson.lock.worker;

import com.wcj.redisson.lock.util.PrintUtil;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by chengjie.wang on 2017/9/30.
 */
public class BaseWorker<T> {

    public void operatorParam(T t){
        Assert.isTrue(t instanceof List,"unknown dataType..");
        List a = (List)t;
        //业务操作1
        a.add(1);
        PrintUtil.printInfo(Thread.currentThread()+"increase size is "+a.size());
        //业务操作2
        PrintUtil.printInfo(Thread.currentThread()+"do some other thing ...");
    }
}

package com.wcj.emp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wcj.emp.dao.Emp;
import com.wcj.emp.mapper.EmpMapper;
import com.wcj.emp.service.EmpRWService;
import com.wcj.redisson.cache.RedissonJCacheManager;
import com.wcj.redisson.lock.util.DistributedReadAndWriteLock;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.expiry.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengjie.wang on 2017/11/6.
 */
@Service
public class EmpServiceRWImpl extends RedissonJCacheManager implements EmpRWService {

    @Autowired
    private EmpMapper empMapper;

    private Cache empCache = null;

    private String cacheName = "EMP_CAHCE_RW";

    private Logger logger = LoggerFactory.getLogger(EmpServiceRWImpl.class);

    private static final String LOCK_NAME = "haodada";

    @PostConstruct
    private void  initCache(){
        empCache = getCache(cacheName, Duration.FIVE_MINUTES);
    }

    public String getEmpFromCache(){
        DistributedReadAndWriteLock.rLock(LOCK_NAME).lock();//上读锁
        Object value = null;
        try {
            value = empCache.get(cacheName);
            logger.info("get data from cache");
            if (null == value) {
                DistributedReadAndWriteLock.rLock(LOCK_NAME).unlock();//读锁释放，上写锁
                logger.info("if null ,get data from database");
                try {
                    DistributedReadAndWriteLock.wLock(LOCK_NAME).lock();//上写锁
                    if(null == value){
                        List<Emp> titles = empMapper.queryEmpInfo();
                        Map<Long, String> map = new HashMap<Long, String>();
                        if (CollectionUtils.isNotEmpty(titles)) {
                            for (Emp dto : titles) {
                                map.put(dto.getEmpno(), dto.getEname());
                            }
                            value = JSONObject.toJSON(map).toString();
                            empCache.put(cacheName, value);
                        }
                    }
                    DistributedReadAndWriteLock.rLock(LOCK_NAME).lock();//上读锁，锁降级
                }finally {
                    DistributedReadAndWriteLock.wLock(LOCK_NAME).unlock();//释放写锁
                }
            }
        }finally {
            DistributedReadAndWriteLock.rLock(LOCK_NAME).unlock();//释放读锁
        }
        return value.toString();
    }
    @Override
    public String queryEmp() {
        return getEmpFromCache();
    }
}

package com.wcj.emp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wcj.emp.dao.Emp;
import com.wcj.emp.mapper.EmpMapper;
import com.wcj.emp.service.EmpService;
import com.wcj.redisson.cache.RedissonJCacheManager;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
public class EmpServiceImpl extends RedissonJCacheManager implements EmpService{

    @Autowired
    private EmpMapper empMapper;

    private Cache empCache = null;

    private String cacheName = "EMP_CAHCE";

    private Logger logger = LoggerFactory.getLogger(EmpServiceImpl.class);

    @PostConstruct
    private void  initCache(){
        empCache = getCache(cacheName, Duration.FIVE_MINUTES);
    }

    public String getEmpFromCache(){
        Object value = empCache.get(cacheName);
        logger.info("get data from cache");
        if(null == value){
            logger.info("if null ,get data from database");
            List<Emp> titles = empMapper.queryEmpInfo();
            Map<Long,String> map = new HashMap<Long,String>();
            if(CollectionUtils.isNotEmpty(titles)){
                for(Emp dto : titles){
                    map.put(dto.getEmpno(),dto.getEname());
                }
                String emp = JSONObject.toJSON(map).toString();
                empCache.put(cacheName,emp);
                return emp;
            }
        }
        return value.toString();
    }
    @Override
    public String queryEmp() {
        return getEmpFromCache();
    }
}

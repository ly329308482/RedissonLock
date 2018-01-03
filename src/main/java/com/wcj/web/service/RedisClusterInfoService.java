package com.wcj.web.service;

import com.wcj.web.exception.DilatationException;
import com.wcj.web.model.po.AddClusterInfo;
import com.wcj.web.model.vo.ClusterNodeInfo;

import java.util.List;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
public interface RedisClusterInfoService {
    /**
     * 获取redis集群节点信息
     * @return
     */
    List<ClusterNodeInfo> getRedisClusterInfo();

    /**
     * 对集群进行扩容
     * @param addClusterInfo
     */
    void addClusterNode(AddClusterInfo addClusterInfo) throws DilatationException;
}

package com.wcj.web.model.vo;

import com.wcj.common.annotation.PropertiesMapping;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
public class ClusterNodeInfo {

    @PropertiesMapping(value = "cluster_known_nodes")
    private String clusterKnownNodes;

    @PropertiesMapping(value = "cluster_my_epoch")
    private String clusterMyEpoch;

    @PropertiesMapping(value = "cluster_size")
    private String clusterSize;

    @PropertiesMapping(value = "cluster_stats_messages_sent")
    private String clusterStatsMessagesSent;

    @PropertiesMapping(value = "cluster_slots_pfail")
    private String clusterSlotsPfail;

    @PropertiesMapping(value = "cluster_stats_messages_received")
    private String clusterStatsMessagesReceived;

    @PropertiesMapping(value = "cluster_current_epoch")
    private String clusterCurrentEpoch;

    @PropertiesMapping(value = "cluster_state")
    private String clusterState;

    @PropertiesMapping(value = "cluster_slots_assigned")
    private String clusterSlotsAssigned;

    @PropertiesMapping(value = "cluster_slots_ok")
    private String clusterSlotsOk;

    @PropertiesMapping(value = "cluster_slots_fail")
    private String clusterSlotsFail;

    public String getClusterKnownNodes() {
        return clusterKnownNodes;
    }

    public void setClusterKnownNodes(String clusterKnownNodes) {
        this.clusterKnownNodes = clusterKnownNodes;
    }

    public String getClusterMyEpoch() {
        return clusterMyEpoch;
    }

    public void setClusterMyEpoch(String clusterMyEpoch) {
        this.clusterMyEpoch = clusterMyEpoch;
    }

    public String getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(String clusterSize) {
        this.clusterSize = clusterSize;
    }

    public String getClusterStatsMessagesSent() {
        return clusterStatsMessagesSent;
    }

    public void setClusterStatsMessagesSent(String clusterStatsMessagesSent) {
        this.clusterStatsMessagesSent = clusterStatsMessagesSent;
    }

    public String getClusterSlotsPfail() {
        return clusterSlotsPfail;
    }

    public void setClusterSlotsPfail(String clusterSlotsPfail) {
        this.clusterSlotsPfail = clusterSlotsPfail;
    }

    public String getClusterStatsMessagesReceived() {
        return clusterStatsMessagesReceived;
    }

    public void setClusterStatsMessagesReceived(String clusterStatsMessagesReceived) {
        this.clusterStatsMessagesReceived = clusterStatsMessagesReceived;
    }

    public String getClusterCurrentEpoch() {
        return clusterCurrentEpoch;
    }

    public void setClusterCurrentEpoch(String clusterCurrentEpoch) {
        this.clusterCurrentEpoch = clusterCurrentEpoch;
    }

    public String getClusterState() {
        return clusterState;
    }

    public void setClusterState(String clusterState) {
        this.clusterState = clusterState;
    }

    public String getClusterSlotsAssigned() {
        return clusterSlotsAssigned;
    }

    public void setClusterSlotsAssigned(String clusterSlotsAssigned) {
        this.clusterSlotsAssigned = clusterSlotsAssigned;
    }

    public String getClusterSlotsOk() {
        return clusterSlotsOk;
    }

    public void setClusterSlotsOk(String clusterSlotsOk) {
        this.clusterSlotsOk = clusterSlotsOk;
    }

    public String getClusterSlotsFail() {
        return clusterSlotsFail;
    }

    public void setClusterSlotsFail(String clusterSlotsFail) {
        this.clusterSlotsFail = clusterSlotsFail;
    }
}

package com.wcj.web.model.po;

/**
 * Created by chengjie.wang on 2018/1/2.
 */
public class AddClusterInfo {

    private String masterIp;

    private String masterPort;

    private String slaveIp;

    private String slavePort;

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    public String getSlaveIp() {
        return slaveIp;
    }

    public void setSlaveIp(String slaveIp) {
        this.slaveIp = slaveIp;
    }


    public String getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(String masterPort) {
        this.masterPort = masterPort;
    }

    public String getSlavePort() {
        return slavePort;
    }

    public void setSlavePort(String slavePort) {
        this.slavePort = slavePort;
    }
}

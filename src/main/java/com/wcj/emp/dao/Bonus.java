package com.wcj.emp.dao;

import java.math.BigDecimal;

/**
 * Created by chengjie.wang on 2017/11/6.
 */
public class Bonus {
    private long empno;
    private String job;
    private BigDecimal sal;
    private BigDecimal comm;

    public long getEmpno() {
        return empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public BigDecimal getSal() {
        return sal;
    }

    public void setSal(BigDecimal sal) {
        this.sal = sal;
    }

    public BigDecimal getComm() {
        return comm;
    }

    public void setComm(BigDecimal comm) {
        this.comm = comm;
    }
}

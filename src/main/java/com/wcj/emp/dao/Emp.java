package com.wcj.emp.dao;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chengjie.wang on 2017/11/6.
 */
public class Emp {
   private long empno;
    private String ename;
    private String job;
    private long mgr;
    private Date hiredate;
    private BigDecimal sal;
    private BigDecimal comm;
    private long deptno;

    public long getEmpno() {
        return empno;
    }

    public void setEmpno(long empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public long getMgr() {
        return mgr;
    }

    public void setMgr(long mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
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

    public long getDeptno() {
        return deptno;
    }

    public void setDeptno(long deptno) {
        this.deptno = deptno;
    }
}

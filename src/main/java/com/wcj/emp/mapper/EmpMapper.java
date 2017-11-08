package com.wcj.emp.mapper;

import com.wcj.emp.dao.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chengjie.wang on 2016/12/19.
 */
@Repository
@Mapper
public interface EmpMapper {
    List<Emp> queryEmpInfo();
}

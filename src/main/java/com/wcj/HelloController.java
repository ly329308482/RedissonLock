package com.wcj;

import com.wcj.emp.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chengjie.wang on 2017/11/6.
 */
@RestController
public class HelloController {
    @Autowired
    private EmpService empService;
    @RequestMapping("hello")
    public String sayHello(){

        return empService.queryEmp();
    }
}

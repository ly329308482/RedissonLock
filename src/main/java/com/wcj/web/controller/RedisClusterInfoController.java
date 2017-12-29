package com.wcj.web.controller;

import com.wcj.web.service.RedisClusterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Controller
public class RedisClusterInfoController {

    @Autowired
    private RedisClusterInfoService redisClusterInfoService;

    @RequestMapping(value = "/getClusterInfo")
    public ModelAndView getClusterInfo(){
        ModelAndView modelAndView = new ModelAndView("Login");
        modelAndView.addObject("redisClusterInfo",redisClusterInfoService.getRedisClusterInfo());
        return modelAndView;
    }

}

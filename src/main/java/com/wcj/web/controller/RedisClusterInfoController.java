package com.wcj.web.controller;

import com.wcj.redisson.lock.manager.RedissionClientManager;
import com.wcj.web.exception.DilatationException;
import com.wcj.web.model.po.AddClusterInfo;
import com.wcj.web.service.RedisClusterInfoService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Controller
public class RedisClusterInfoController {

    @Autowired
    private RedisClusterInfoService redisClusterInfoService;


    /**
     * 主页面
     * @return
     */
    @RequestMapping(value = "/getClusterInfo")
    public ModelAndView getClusterInfo(){
        ModelAndView modelAndView = new ModelAndView("clusterInfo");
        modelAndView.addObject("redisClusterInfo",redisClusterInfoService.getRedisClusterInfo());
        return modelAndView;
    }


    /**
     * 编辑页面
     * @return
     */
    @RequestMapping(value = "/editClusterInfo")
    public ModelAndView editClusterInfo(){
        ModelAndView modelAndView = new ModelAndView("addclusterInfo");
        return modelAndView;
    }

    /**
     * 扩容
     * @param addClusterInfo
     * @return
     */
    @RequestMapping(value = "/addClusterInfo")
    @ResponseBody
    public Map<String ,String> addClusterInfo(@ModelAttribute AddClusterInfo addClusterInfo){
        Map<String ,String> mapout = new HashMap<>();
        try {
            mapout.put("messageCode","0");
            mapout.put("messageTxt","扩容成功");
            redisClusterInfoService.addClusterNode(addClusterInfo);
        }catch (Exception e){
            if(e instanceof DilatationException){
                DilatationException de = (DilatationException)e;
                mapout.put("messageTxt",de.toString());
                mapout.put("messageCode","-1");
            }
        }
        return mapout;
    }

}

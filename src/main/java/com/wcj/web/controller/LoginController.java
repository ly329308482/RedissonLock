package com.wcj.web.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    /**
     * 登录提交
     * @param request		request，用来取登录之前Url地址，用来登录后跳转到没有登录之前的页面。
     * @return
     */
    @RequestMapping(value="submitLogin",method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitLogin( HttpServletRequest request){
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        return null;
    }
}

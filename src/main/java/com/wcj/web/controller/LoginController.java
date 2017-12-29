package com.wcj.web.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by chengjie.wang on 2017/12/29.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String login(){
        return "Login";
    }

}

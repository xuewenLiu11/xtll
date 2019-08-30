package com.xtll.web.controller.system;


import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginOutController {



    @RequestMapping("/logout")
    @ResponseBody
    public Map<String,Object> logout(){
        System.out.println("退出登录");
        Map<String,Object> map=new HashMap<>();

        try{
            SecurityUtils.getSubject().logout();

        }catch (Exception e){
            map.put("code","-1");
            map.put("msg","退出失败");
            return map;
        }
        map.put("code","1");
        map.put("msg","退出成功");
        return map;
    }




}

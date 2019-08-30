package com.xtll.web.controller.system;


import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.json.JSONObject;
import com.xtll.common.utils.StringUtils;
import com.xtll.system.service.AgtUserService;
import com.xtll.system.service.AllRoleService;
import com.xtll.system.service.BussUserService;
import com.xtll.system.service.XtUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;

@Api("删除操作")
@Controller
public class DelController {
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private XtUserService xtUserService;

    @Autowired
    private AgtUserService agtUserService;

    @Autowired
    private BussUserService bussUserService;


    @Autowired
    private AllRoleService allRoleService;


    @ApiOperation("删除类型")
    @ApiImplicitParams(
            { @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "query", dataType = "Integer")
            }
    )
    @ResponseBody
    @PostMapping(value = "/sys/del/role")
    public Map<String, Object> delRole(@RequestBody  JSONObject id){

        System.out.println(id);
        String id1= id.getStr("id");
        Map<String,Object> map=new HashMap<>();
        try {
                int num=allRoleService.deleteByPrimaryKey(Integer.parseInt(id1));
                System.out.println(num);
                if(num<=0){
                    //2 代表由于其他问题，删除失败
                    map.put("code","-1");
                    map.put("msg","没有此角色");
                    return map;
                }else{
                    map.put("code","1");
                    map.put("msg","删除成功");
                }
        }catch (Exception e){
            map.put("code","0");
            map.put("msg","id获取异常");
            return map;
        }

        return map;
    }

    /**
     * 管理员删除
     * @param json
     * @return
     */
    @ApiOperation("管理员删除")
    @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "query", dataType = "String")
    @ResponseBody
    @PostMapping("/sys/del/user")
    public Map<String, Object> delUSer(@RequestBody  JSONObject json){

        Map<String,Object> map=new HashMap<>();
        String loginName=json.getStr("loginName");
        System.out.println(loginName);

        try {
            if(!StringUtils.isEmpty(loginName)){
                int num=xtUserService.deleteByLoginName(loginName);

                if(num>0){
                    // 1代表删除成功
                    map.put("code","1");
                    map.put("msg","删除成功");
            }
        }else{
                map.put("code","-1");
                map.put("msg","没有此用户名");
                return map;

            }
        }catch (Exception e){
            map.put("code","-1");
            map.put("msg","删除失败");
            log.error(e.getMessage());
            return map;
        }
        return map;
    }



    @ApiOperation("代理商删除")
    @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "query", dataType = "String")
    @ResponseBody
    @PostMapping("/sys/del/agt")
    public Map<String, Object> delBuss(@RequestBody JSONObject json){
        Map<String,Object> map=new HashMap<>();
        String loginName=json.getStr("loginName");
        System.out.println(loginName);
        try {
            if(!StringUtils.isEmpty(loginName)){
                int num=agtUserService.deleteByLoginName(loginName);

                if(num>0){
                    // 1代表删除成功
                    map.put("code","1");
                    map.put("msg","删除成功");
                }
            }else{
                map.put("code","-1");
                map.put("msg","没有此用户名");
                //0代表该用户名不存在
                return  map;

            }

        }catch (Exception e){
            map.put("code","-1");
            map.put("msg","删除失败");
            log.error(e.getMessage());
            return map;
        }
        return map;
    }

    @ApiOperation("商户删除")
    @ApiImplicitParam(name = "loginName", value = "登录名", required = true, paramType = "query", dataType = "String")
    @ResponseBody
    @PostMapping("/sys/del/buss")
    public Map<String, Object> delAgt(@RequestBody JSONObject json){

        Map<String,Object> map=new HashMap<>();
        String loginName=json.getStr("loginName");
        System.out.println(loginName);

        try{

            if(!StringUtils.isEmpty(loginName)){
                int num=bussUserService.deleteByLoginName(loginName);

                if(num>0){
                    // 1代表删除成功
                    map.put("code","1");
                    map.put("msg","删除成功");

                }
            }else{
                map.put("code","-1");
                map.put("msg","没有此用户名");
                return map;
            }
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("code","-1");
            map.put("msg","删除失败");
            return map;
        }
        return map;
    }

}

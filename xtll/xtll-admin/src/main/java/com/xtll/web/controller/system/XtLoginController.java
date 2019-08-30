package com.xtll.web.controller.system;

import com.xtll.common.core.controller.BaseController;
import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.utils.ServletUtils;
import com.xtll.common.utils.StringUtils;
import com.xtll.system.domain.AllRole;
import com.xtll.system.domain.XtUser;
import com.xtll.system.service.AllRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Api(value = "用户登录")
@RestController
public class XtLoginController extends  BaseController {


    @Autowired
    private AllRoleService allRoleService;

        @GetMapping("/login")
        public String login(HttpServletRequest request, HttpServletResponse response)
        {

            System.out.println("走login");
            HashMap<String,Object> map=new HashMap<>();
            // 如果是Ajax请求，返回Json字符串。
            //如果session没有

            if (ServletUtils.isAjaxRequest(request))
            {
                System.out.println("是ajax请求");
                return ServletUtils.renderString(response, "{\"code\":\"2\",\"msg\":\"未登录或登录超时。请重新登录\"}");
            }
            //System.out.println("退出登录、login");
            return "login";

        }
        //@CrossOrigin(origins = "http://192.168.1.119:8081",maxAge = 3600)
        @ApiOperation(value="登录入口")
        @ApiImplicitParams(
                { @ApiImplicitParam(name = "loginName", value = "用户名", required = true, paramType = "query", dataType = "String"),
                @ApiImplicitParam(name="password",value="密码",required = true,paramType = "query",dataType = "String")}
                )
        //通過ajax傳入到這個controller  post方法
        @PostMapping("/login")
        @ResponseBody
        public Map<String, Object> ajaxLogin(@RequestBody XtUser user)
        {
            Map<String,Object> map=new HashMap<>();
            System.out.println("正在登陆。。。。。。。。。。。。。。");

            Boolean rememberMe=true;
            UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(),user.getPassword(), rememberMe);
            Subject subject = SecurityUtils.getSubject();

            try
            {
                subject.login(token);
                XtUser user1=(XtUser) subject.getPrincipal();
                AllRole allRole = allRoleService.selectByPrimaryKey(user1.getRoleId());
                user1.setRoleName(allRole.getRole());
                System.out.println("principal======"+user1);
                System.out.println("延时开始");
               // Thread.sleep(3000);
                map.put("loginUser",user1);
                map.put("code",1);
                map.put("msg","登陆成功");
                System.out.println("延时结束");

                return map;
            }
            catch (AuthenticationException e)
            {
                String msg = "用户或密码错误";
                if (StringUtils.isNotEmpty(e.getMessage()))
                {
                    msg = e.getMessage();
                }
                return error(msg);
            }
        }

        @GetMapping("/unauth")
        public String unauth()
        {
            return "error/unauth";
        }


}

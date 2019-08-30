package com.xtll.framework.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.xtll.common.annotation.RepeatSubmit;
import com.xtll.common.core.domain.AjaxResult;
import com.xtll.common.json.JSON;
import com.xtll.common.utils.ServletUtils;

/**
 * 防止重复提交拦截器
 * 
 * @author xtll
 */
@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        /*// 允许客户端携带跨域cookie
        // 当Access-Control-Allow-Credentials设为true的时候，Access-Control-Allow-Origin不能设为星号

        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许指定域访问跨域资源
        //response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:9006, http://127.0.0.1:8080");
        response.setHeader("Access-Control-Allow-Origin", "http://192.168.1.119:8081");// *
        // 允许浏览器发送的请求消息头
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));// *
        // 允许浏览器在预检请求成功之后发送的实际请求方法名
        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
        // 设置响应数据格式
        response.setHeader("Content-Type", "application/json");
        // 查看请求方法
        String method1= request.getMethod();
        System.out.println("请求方法"+method1);*/



        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null)
            {
                if (this.isRepeatSubmit(request))
                {
                    AjaxResult ajaxResult = AjaxResult.error("不允许重复提交，请稍后再试");
                    ServletUtils.renderString(response, JSON.marshal(ajaxResult));
                    return false;
                }
            }
            return true;
        }
        else
        {
            return super.preHandle(request, response, handler);
        }

    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request) throws Exception;

    private String getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        String result="";
        for (String key : map.keySet()) {
            //System.out.println("key= "+ key + " and value= " + map.get(key));
            result = result + "key= "+ key + " and value= " + map.get(key)+"\n";
        }
        System.out.println("请求头       "+result);
        return result;
    }

}

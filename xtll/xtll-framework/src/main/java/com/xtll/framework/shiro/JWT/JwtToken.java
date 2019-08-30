package com.xtll.framework.shiro.JWT;


import org.apache.shiro.authc.AuthenticationToken;


/**
 * 类似于javaBean  自定义对象来包装token
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
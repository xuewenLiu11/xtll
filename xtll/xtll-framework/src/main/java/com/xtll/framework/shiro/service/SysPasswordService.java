package com.xtll.framework.shiro.service;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;

import com.xtll.system.domain.XtUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.xtll.common.constant.Constants;
import com.xtll.common.constant.ShiroConstants;
import com.xtll.common.exception.user.UserPasswordNotMatchException;
import com.xtll.common.exception.user.UserPasswordRetryLimitExceedException;
import com.xtll.common.utils.MessageUtils;
import com.xtll.framework.manager.AsyncManager;
import com.xtll.framework.manager.factory.AsyncFactory;
import com.xtll.system.domain.SysUser;

/**
 * 登录密码方法
 * 
 * @author xtll
 */
@Component
public class SysPasswordService
{
    @Autowired
    private CacheManager cacheManager;

    private Cache<String, AtomicInteger> loginRecordCache;

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    @PostConstruct
    public void init()
    {
        loginRecordCache = cacheManager.getCache(ShiroConstants.LOGINRECORDCACHE);
    }

    public void validate(XtUser user, String password)
    {

        System.out.println("进入密码验证 和加密设置");
        String loginName = user.getLoginName();

        AtomicInteger retryCount = loginRecordCache.get(loginName);

        if (retryCount == null)
        {
            retryCount = new AtomicInteger(0);
            loginRecordCache.put(loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue())
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }

        if (!matches(user, password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            loginRecordCache.put(loginName, retryCount);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(loginName);
        }
    }

    public boolean matches(XtUser user, String newPassword)
    {

        System.out.println("加密前："+newPassword);
        //盐值用了用户名
       return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword));
       // return  user.getPassword().equals(newPassword);
    }

    public void clearLoginRecordCache(String username)
    {
        System.out.println("进入缓存移除");
        loginRecordCache.remove(username);
    }

    public String encryptPassword(String username, String password)
    {

        String algorithmName="MD5";
        //加密的字符串
        String source=username+password;
        //盐值，用于和密码混合起来用
        ByteSource salt = ByteSource.Util.bytes(username);
        //加密的次数,可以进行多次的加密操作
        int hashIterations = 3;
        //通过SimpleHash 来进行加密操作
        SimpleHash hash = new SimpleHash(algorithmName, source, salt, hashIterations);
        String newPass=hash.toString();
        //String newPass=new Md5Hash(username + password + salt).toHex().toString();

        System.out.println("加密后"+newPass);
        return newPass;
    }

}

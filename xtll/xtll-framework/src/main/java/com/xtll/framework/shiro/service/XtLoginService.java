package com.xtll.framework.shiro.service;

import com.xtll.common.constant.Constants;
import com.xtll.common.constant.ShiroConstants;
import com.xtll.common.constant.UserConstants;
import com.xtll.common.enums.UserStatus;
import com.xtll.common.exception.user.*;
import com.xtll.common.utils.DateUtils;
import com.xtll.common.utils.MessageUtils;
import com.xtll.common.utils.ServletUtils;
import com.xtll.framework.manager.AsyncManager;
import com.xtll.framework.manager.factory.AsyncFactory;
import com.xtll.framework.util.ShiroUtils;
import com.xtll.system.domain.SysUser;
import com.xtll.system.domain.XtUser;
import com.xtll.system.service.ISysUserService;
import com.xtll.system.service.XtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 登录校验方法
 * 
 * @author xtll
 */
@Component
public class XtLoginService
{
    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private XtUserService xtUserService;

    /**
     * 登录
     */
    public XtUser login(String username, String password)
    {

        System.out.println("进入登录服务，相关验证");
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }

        // 查询用户信息
        XtUser user = xtUserService.selectByLoginName(username);
        System.out.println("根据用户名查询用户"+user);


        if (user == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        


        //状态判断  0-可以用 1-停用
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked", user.getRemark())));
            throw new UserBlockedException();
        }

        passwordService.validate(user, password);

        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
       // recordLoginInfo(user);
        return user;
    }



    private boolean maybeMobilePhoneNumber(String username)
    {
        if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN))
        {
            return false;
        }
        return true;
    }

    /**
     * 记录登录信息
     */
  /*  public void recordLoginInfo(XtUser user)
    {
        user.setLoginIp(ShiroUtils.getIp());
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserInfo(user);
    }*/
}

package com.hgt.project.web.execption;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
public class LoginExceptionHandler {

    public static String getMessage(RuntimeException e) {
        String resultMassage;
        if (e instanceof IncorrectCredentialsException) {
            resultMassage = "密码错误";
        } else if (e instanceof  LockedAccountException) {
            resultMassage = "账户已锁定";
        } else if (e instanceof ExcessiveAttemptsException) {
            resultMassage = "超过尝试次数";
        } else if (e instanceof UnknownAccountException) {
            resultMassage = "账号不存在";
        } else {
            resultMassage = e.getMessage();

        }
        return resultMassage;
    }

}

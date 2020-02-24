package com.hgt.project.web.execption;

import com.hgt.project.common.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Slf4j
@ControllerAdvice
public class ShirExceptionHandler {

    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public ResponseEntity<Response> doHandlerShirException(ShiroException e) {
        log.error(e.getMessage());
        Response response = new Response();
        response.setSuccess(false);
        if (e instanceof IncorrectCredentialsException) {
            response.setMessage("密码正确");
        } else if (e instanceof UnknownAccountException) {
            response.setMessage("此用户不存在");
        } else if (e instanceof LockedAccountException) {
            response.setMessage("账户已被禁用");
        }
        response.setBody(e.getMessage());
        return ResponseEntity.ok(response);
    }

}

package com.hgt.project.web.controller;

import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Employee;
import com.hgt.project.web.execption.LoginExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Controller
@Slf4j
public class HomeIndexController {

    @GetMapping(path = {"/", "/login"})
    public String defaultLogin() {
        return "login";
    }

    @PostMapping(path = "/login")
    @ResponseBody
    public ResponseEntity<Response> defaultLogin(Employee employee) {
        Map<String, Object> params = new HashMap<>(2);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(employee.getUsername(), employee.getPassword());
        token.setRememberMe(true);
        try {
            subject.login(token);
            params.put("token", subject.getSession().getId());
            params.put("username", token.getUsername());
        } catch (UnknownAccountException
                | IncorrectCredentialsException
                | LockedAccountException
                | ExcessiveAttemptsException e) {
            return ResponseEntity.ok().body(new Response(false, LoginExceptionHandler.getMessage(e)));
        }
        if (subject.isAuthenticated()) {
            return ResponseEntity.ok().body(new Response(true, "登陆成功", params));
        } else {
            token.clear();
            return ResponseEntity.ok().body(new Response(true, "登陆失败", params));
        }
    }

    @GetMapping(path = "/logout")
    public void defaultLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Value("${file-path}")
    private String path;

    @GetMapping(path = "/yml")
    @ResponseBody
    public String getYml() {
        return path;
    }
}

package com.hgt.project.dao.util;

import com.hgt.project.dao.entity.Employee;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public class UserUtil {

	public static Employee getCurrentUser() {
		Employee user = null;
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			user = (Employee) subject.getPrincipal();
		}
        return user;
	}
	
	public static Long getCurrentUserId() {
		Long userId = null;
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			Employee user = (Employee) subject.getPrincipal();
			userId = user.getId();
		}
        return userId;
	}

	public static String getCurrentUsername() {
		String username = null;
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			if (subject.getPrincipal() != null) {
				Employee user = (Employee) subject.getPrincipal();
				username = user.getUsername();
			}
		}
		return username;
	}

	public static Employee getUserByToken(String token, HttpServletRequest request, HttpServletResponse response) {
		Employee user = null;
		SessionKey key = new WebSessionKey(token, request, response);
		try {
			Session session = SecurityUtils.getSecurityManager().getSession(key);
			Object obj = session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
			if ((Boolean)obj) {
				Object pObj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				SimplePrincipalCollection coll = (SimplePrincipalCollection) pObj;
				return (Employee) coll.getPrimaryPrincipal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return user;
	}
}

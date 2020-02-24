package com.hgt.project.web.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hgt.project.dao.entity.Employee;
import com.hgt.project.service.IEmployeeService;
import com.hgt.project.service.IRoleService;
import freemarker.core.BugException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Slf4j
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IRoleService iRoleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles;
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if (Objects.isNull(username)) {
            throw new BugException("未登录");
        }
        wrapper.eq("username", username);
        Employee employee = employeeService.getOne(wrapper);
        roles = iRoleService.setByUsername(employee.getUsername());
        info.setRoles(roles);
        info.setObjectPermissions(null);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNoneBlank(username), "username", username);
        Employee employee = employeeService.getOne(wrapper);
        if (Objects.isNull(employee)) {
            return null;
        }
        String password = employee.getPassword();
        ByteSource salt = ByteSource.Util.bytes(employee.getSalt());
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                employee.getUsername(),
                password,
                salt,
                getName()
        );
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("USER_SESSION", employee);
        return authenticationInfo;
    }
}

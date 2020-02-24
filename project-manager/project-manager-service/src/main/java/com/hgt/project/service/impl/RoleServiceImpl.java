package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Role;
import com.hgt.project.dao.mapper.RoleMapper;
import com.hgt.project.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Set<String> setByUsername(String username) {
        return roleMapper.setByUsername(username);
    }
}

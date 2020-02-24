package com.hgt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.Role;

import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IRoleService extends IService<Role> {

    Set<String> setByUsername(String username);

}

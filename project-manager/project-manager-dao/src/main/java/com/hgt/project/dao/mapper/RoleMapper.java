package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.Role;

import java.util.Set;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface RoleMapper extends BaseMapper<Role> {

    Set<String> setByUsername(String username);

}

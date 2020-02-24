package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Role;
import com.hgt.project.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author tiaguifang
 * @since 2020-01-06
 */
@RestController
@RequestMapping("/project/role")
@Slf4j
@Api(tags = "系统管理--角色管理")
public class RoleController {

	private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(value = "角色信息列表", httpMethod = "GET")
    @GetMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "roleCode", required = false, defaultValue = "") String roleCode) {
        log.info("分页查询角色信息");
        Page<Role> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(roleCode),"role_code", roleCode);
        IPage<Role> mapIPage;
        try {
            mapIPage = roleService.page(page, wrapper);
        } catch (Exception e) {
        	log.error("查询角色信息报错：", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "角色分页服务记录", mapIPage));
    }
	
    @ApiOperation(value = "新增角色", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Role role) {
        log.info("添加角色");
        try {
        	// 验证角色名称
        	if (StringUtils.isBlank(role.getRoleName())) {
        		return ResponseEntity.ok().body(new Response(false, "角色名称不能为空"));
        	}
        	if (role.getRoleName().length() > 10) {
        		return ResponseEntity.ok().body(new Response(false, "角色名称不能超过10个字符"));
        	}
        	QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("role_name", role.getRoleName().trim());
        	List<Role> list = roleService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(list)) {
        		return ResponseEntity.ok().body(new Response(false, "角色名称已经存在"));
        	}
        	// 验证角色编码
        	if (StringUtils.isBlank(role.getRoleCode())) {
        		return ResponseEntity.ok().body(new Response(false, "角色编码不能为空"));
        	}
        	if (role.getRoleCode().length() > 10) {
        		return ResponseEntity.ok().body(new Response(false, "角色编码不能超过10个字符"));
        	}
        	queryWrapper= new QueryWrapper<>();
        	queryWrapper.eq("role_Code", role.getRoleCode().trim());
        	list = roleService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(list)) {
        		return ResponseEntity.ok().body(new Response(false, "角色编码已经存在"));
        	}
        	
        	role.setRoleName(role.getRoleName().trim());
        	role.setRoleCode(role.getRoleCode().trim());
        	//role.setStatus(1);
            roleService.save(role);
        } catch (Exception e) {
        	log.error("保存角色信息失败", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存投标信息成功！", role.getId()));
    }

    @ApiOperation(value = "禁用，启用角色信息", httpMethod = "PUT")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", dataType = "Long", value = "角色ID"),
        @ApiImplicitParam(name = "status", dataType = "int", value = "角色状态：0禁用  1启用"),
    })
    @PutMapping(value = "/update")
    public ResponseEntity<Response> update(Long id, Integer status) {
        log.info("更新角色信息");
        try {
        	if (id == null || status == null || (status != 0 && status != 1)) {
        		return ResponseEntity.ok().body(new Response(false, "参数错误"));
        	}
        	Role role = new Role();
        	role.setId(id);
        	role.setStatus(status);
        	roleService.updateById(role);
        } catch (Exception e) {
        	log.error("更新角色信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新角色信息成功！"));
    }

    @ApiOperation(value = "删除角色信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除角色信息");
        try {
        	roleService.removeByIds(ids);
        } catch (Exception e) {
        	log.error("删除角色信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除角色信息成功！", ids));
    }
    
}

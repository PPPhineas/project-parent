package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Department;
import com.hgt.project.service.IDepartmentService;
import io.swagger.annotations.Api;
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
 * 部门 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2020-01-06
 */
@RestController
@RequestMapping("/project/department")
@Slf4j
@Api(tags = "系统管理--组织机构管理")
public class DepartmentController {

	private final IDepartmentService departmentService;

	public DepartmentController(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@ApiOperation(value = "组织机构信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list() {
        log.info("分页查询组织机构信息");
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Department> mapIPage;
        try {
            mapIPage = departmentService.list(wrapper);
        } catch (Exception e) {
        	log.error("查询组织机构信息报错：", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "组织机构所有记录", mapIPage));
    }
	
    @ApiOperation(value = "新增组织机构", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Department department) {
        log.info("添加角色");
        try {
        	// 验证机构名称
        	if (StringUtils.isBlank(department.getDepartmentName())) {
        		return ResponseEntity.ok().body(new Response(false, "机构名称不能为空"));
        	}
        	if (department.getDepartmentName().trim().length() > 15) {
        		return ResponseEntity.ok().body(new Response(false, "机构名称不能超过15个字符"));
        	}
        	QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("department_name", department.getDepartmentName().trim());
        	List<Department> list = departmentService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(list)) {
        		return ResponseEntity.ok().body(new Response(false, "机构名称已经存在"));
        	}
        	// 验证机构编码
        	if (StringUtils.isBlank(department.getDepartmentCode())) {
        		return ResponseEntity.ok().body(new Response(false, "机构编码不能为空"));
        	}
        	if (department.getDepartmentCode().trim().length() > 10) {
        		return ResponseEntity.ok().body(new Response(false, "机构编码不能超过10个字符"));
        	}
        	queryWrapper= new QueryWrapper<>();
        	queryWrapper.eq("department_Code", department.getDepartmentCode().trim());
        	list = departmentService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(list)) {
        		return ResponseEntity.ok().body(new Response(false, "机构编码已经存在"));
        	}
        	
        	department.setDepartmentName(department.getDepartmentName().trim());
        	department.setDepartmentCode(department.getDepartmentCode().trim());
            departmentService.save(department);
        } catch (Exception e) {
        	log.error("保存组织机构信息失败", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存组织机构信息成功！", department.getId()));
    }

    
    @ApiOperation(value = "更新组织机构信息，更新机构名称和父级机构", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody Department department) {
        log.info("更新组织机构信息");
        try {
        	if (StringUtils.isBlank(department.getDepartmentName())) {
        		return ResponseEntity.ok().body(new Response(false, "结构名称不能为空"));
        	}
        	Department tempDepartment = departmentService.getById(department.getId());
        	// 如果机构名称改变，则需验证机构名称是否已经存在
        	if (!tempDepartment.getDepartmentName().equals(department.getDepartmentName())) {
        		QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
            	queryWrapper.eq("department_name", department.getDepartmentName().trim());
            	List<Department> list = departmentService.list(queryWrapper);
            	if (CollectionUtils.isNotEmpty(list)) {
            		return ResponseEntity.ok().body(new Response(false, "机构名称已经存在"));
            	}
        	}
        	department.setDepartmentName(department.getDepartmentName().trim());
        	departmentService.updateById(department);
        } catch (Exception e) {
        	log.error("更新组织机构信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新组织机构信息成功！"));
    }

    @ApiOperation(value = "删除组织机构信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除组织机构信息");
        try {
        	departmentService.removeByIds(ids);
        } catch (Exception e) {
        	log.error("删除组织机构信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除组织机构信息成功！", ids));
    }
}

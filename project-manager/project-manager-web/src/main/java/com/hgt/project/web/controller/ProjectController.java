package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Employee;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectData;
import com.hgt.project.dao.entity.ProjectEmployee;
import com.hgt.project.dao.entity.vo.ProjectAndEmployeeVO;
import com.hgt.project.dao.util.ExcelUtil;
import com.hgt.project.service.IEmployeeService;
import com.hgt.project.service.IProjectDataService;
import com.hgt.project.service.IProjectEmployeeService;
import com.hgt.project.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 项目 前端控制器
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/project/project")
@Slf4j
@Api(tags = "项目信息管理")
public class ProjectController {

    private List<OrderItem> orderItems = new ArrayList<>();

    private final IProjectService projectService;

    private final IProjectDataService projectDataService;

    private final IProjectEmployeeService projectEmployeeService;

    private final IEmployeeService employeeService;

    public ProjectController(IProjectService projectService, IProjectEmployeeService projectEmployeeService, IEmployeeService employeeService, IProjectDataService projectDataService) {
        this.projectService = projectService;
        this.projectEmployeeService = projectEmployeeService;
        this.employeeService = employeeService;
        this.projectDataService = projectDataService;
    }

    @ApiOperation(value = "项目列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", dataType = "int", value = "当前页"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "页大小"),
            @ApiImplicitParam(name = "projectName", dataType = "String", value = "项目名称")
    })
    @GetMapping(path = "/page")
    @RequiresRoles(value = {"admin", "125"}, logical = Logical.OR)
    @RequiresPermissions(value = {"select"}, logical = Logical.AND)
    public ResponseEntity<Response> list(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                         @RequestParam(value = "projectName", required = false, defaultValue = "") String projectName
    ) {
        log.info("分页查询项目信息");
        Page<Project> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        orderItems.clear();
        // 这里排序使用的字段名称对应数据库的字段名称
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(projectName),"project_name", projectName);
        IPage<Project> mapIPage;
        try {
            mapIPage = projectService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "分页项目记录", mapIPage));
    }

    @ApiOperation(value = "添加项目", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ProjectAndEmployeeVO vo) {
        log.info("添加项目信息");
        boolean result;
        ProjectData projectData;
        try {
            // 1.保存项目信息
            projectService.save(vo.getProject());
            // 2.保存项目参与人员信息
            projectEmployeeService.saveBatch(vo.getProjectEmployees());
            // 3.创建项目资料目录
            projectData = new ProjectData(vo.getProject());
            result = projectDataService.saveWithFile(projectData);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "保存项目信息成功！", vo));
        }
        return ResponseEntity.ok().body(new Response(false, "创建项目资料目录失败！", projectData));
    }

    @ApiOperation(value = "更新项目", httpMethod = "PUT")
    @PutMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody ProjectAndEmployeeVO vo) {
        log.info("更新项目信息");
        QueryWrapper<ProjectEmployee> wrapper = new QueryWrapper<>();
        wrapper.eq("project_number", vo.getProject().getProjectNumber());
        Project origin;
        boolean result;
        try {
            // 0.根据项目id获取原项目
            origin = projectService.getById(vo.getProject().getId());
            // 1.更新项目信息
            projectService.updateById(vo.getProject());
            // 2.更新项目成员信息
            if (Objects.nonNull(vo.getProject().getProjectNumber())) {
                projectEmployeeService.remove(wrapper);
                projectEmployeeService.saveBatch(vo.getProjectEmployees());
            }
            // 3.更新项目资料
            result = projectDataService.updateWithFileByProject(origin, vo.getProject());
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "更新项目资料信息成功！", vo));
        }
        return ResponseEntity.ok().body(new Response(false, "更新项目资料信息失败！", vo));
    }

    @ApiOperation(value = "删除项目", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam(value = "numbers", required = false) Set<String> numbers) {
        log.info("删除项目信息");
        QueryWrapper<ProjectEmployee> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Project> projectQueryWrapper = new QueryWrapper<>();
        boolean result = false;
        try {
            for (String number: numbers) {
                queryWrapper.eq("project_number", number);
                projectQueryWrapper.eq("project_number", number);
                // 1.删除项目参与人员
                if (numbers.size() > 0) {
                    projectEmployeeService.remove(queryWrapper);
                }
                // 2.删除项目资料
                Project project = projectService.getOne(projectQueryWrapper);
                result = projectDataService.removeWithFileByProject(project);
                // 3.删除项目
                projectService.remove(projectQueryWrapper);
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "删除项目信息成功！", numbers));
        }
        return ResponseEntity.ok().body(new Response(false, "删除项目资料信息失败！", numbers));
    }

    @ApiOperation(value = "获取项目成员", httpMethod = "GET")
    @GetMapping("/project-employees/{projectNumber}")
    public ResponseEntity<Response> getProjectEmployeeList(@PathVariable(value = "projectNumber") String projectNumber) {
        log.info("通过项目编号获取项目成员");
        List<Employee> employeeList;
        try {
            employeeList = employeeService.getEmployeeByProjectNumber(projectNumber);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "获取项目成员信息成功！", employeeList));
    }

    @ApiOperation(value = "项目年限区间", httpMethod = "GET")
    @GetMapping(value = "/years")
    public ResponseEntity<Response> getYears() {
        log.info("获取项目年限区间");
        Map<String, Object> map = new HashMap<>(2);
        QueryWrapper<Project> minYearWrapper = new QueryWrapper<>();
        List<Project> projects;
        try {
            minYearWrapper.orderByDesc("project_time");
            projects = projectService.list(minYearWrapper);
            map.put("maxYear", projects.get(0).getProjectTime().getYear());
            map.put("minYear", projects.get(projects.size() - 1).getProjectTime().getYear());
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "获取项目年限区间信息成功！", map));
    }

    @ApiOperation(value = "项目信息导出", httpMethod = "GET")
    @GetMapping(value = "export")
    public void exportProject(HttpServletResponse response) {
        log.info("导出项目信息");
        List<Project> projects;
        try {
            projects = projectService.list();
            ExcelUtil.exportProjects(response, projects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "项目信息导入", httpMethod = "POST")
    @PostMapping(value = "import")
    public ResponseEntity<Response> importProject(MultipartFile file,Integer importType) {
        log.info("项目信息导入");
        List<Project> projectList;
        try {
            projectList = projectService.importProjectInfo(file, importType);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "导入项目信息成功！", projectList));
    }

    @ApiOperation(value = "检查项目编号", httpMethod = "GET")
    @GetMapping(value = "check")
    public ResponseEntity<Response> checkNumber(@RequestParam String projectNumber) {
        log.info("检查项目编号是否唯一");
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        Project project;
        try {
            queryWrapper.eq("project_number", projectNumber);
            project = projectService.getOne(queryWrapper);
            if (Objects.nonNull(project)) {
                return ResponseEntity.ok().body(new Response(false, "存在相同项目编号", projectNumber));
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "新的项目编号", projectNumber));
    }
}

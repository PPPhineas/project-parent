package com.hgt.project.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.ProjectIssue;
import com.hgt.project.service.IProjectIssueService;
import com.hgt.project.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="/project/project_issue")
@Slf4j
@Api(tags="项目问题")
public class ProjectIssueController {
    private List<OrderItem> orderItems = new ArrayList<>();
    private final IProjectIssueService projectIssueService;
    private final IProjectService projectService;
    public ProjectIssueController(IProjectService projectService,IProjectIssueService projectIssueService) {
        this.projectService=projectService;
        this.projectIssueService=projectIssueService;}

    @ApiOperation(value = "查询项目问题", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", dataType = "int", value = "当前页"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "页大小"),
            @ApiImplicitParam(name = "project_number", dataType = "String", value = "项目编号")
    })
    @GetMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                         @RequestParam(value = "project_number", required = false, defaultValue = "") String project_number
    ) {
        log.info("分页查询问题内容信息");
        Page<ProjectIssue> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        orderItems.clear();
        // 这里排序使用的字段名称对应数据库的字段名称
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        QueryWrapper<ProjectIssue> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(project_number),"project_number", project_number);
        IPage<ProjectIssue> mapIPage;
        try {
            mapIPage = projectIssueService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "分页服务记录", mapIPage));
    }

    @ApiOperation(value = "添加项目问题", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ProjectIssue projectIssue){
        log.info("添加项目问题信息");
        try {
            if (StringUtils.isBlank(projectIssue.getDescr())) {
                return ResponseEntity.ok().body(new Response(false, "项目问题描述不能为空！"));
            }
            if (StringUtils.isBlank(projectService.whetherExists(projectIssue.getProjectNumber()))) {
                return ResponseEntity.ok().body(new Response(false, "项目编号为空或者不存在！", projectIssue));
            }

            if (projectIssue.getDescr().length() > 200/3) {
                return ResponseEntity.ok().body(new Response(false, "项目问题描述长度不能超过200个字符！"));
            }
            if (StringUtils.isNotBlank(projectIssue.getSolution())) {
                if (projectIssue.getSolution().length() > 200/3) {
                    return ResponseEntity.ok().body(new Response(false, "项目问题解决方案长度不能超过200个字符！"));
                }
            }
            projectIssueService.saveProjectIssue(projectIssue);
            /*projectService.icsCount("issue_count",projectIssue.getProjectNumber());*/
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存项目问题成功！", projectIssue));
    }

    @ApiOperation(value = "更新项目问题", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody ProjectIssue projectIssue) {
        log.info("更新项目问题信息");
        try {
            if(StringUtils.isNotBlank(projectIssue.getSolution())){
                if (projectIssue.getSolution().length() > 200/3){
                    return ResponseEntity.ok().body(new Response(false, "项目解决方案描述不能超过200个字符！"));
                }
            }
            projectIssueService.updateById(projectIssue);
        } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.ok().body(new Response(false, e.getMessage()));
            }
            return ResponseEntity.ok().body(new Response(true, "保存项目问题成功！", projectIssue));
    }

    @ApiOperation(value = "删除问题", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Long> ids) {
        log.info("删除项目问题信息");
        try {
            projectIssueService.deleteIssueByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除项目问题信息成功！", ids));
    }
}
package com.hgt.project.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.ProjectRisk;
import com.hgt.project.service.IProjectRiskService;
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
@RequestMapping(path="/project/project_risk")
@Slf4j
@Api(tags="项目风险")
public class ProjectRiskController {
    private List<OrderItem> orderItems = new ArrayList<>();
    private final IProjectRiskService projectRiskService;
    private final IProjectService projectService;
    public ProjectRiskController(IProjectService projectService,IProjectRiskService projectRiskService) {
        this.projectService=projectService;
        this.projectRiskService=projectRiskService;
    }

    @ApiOperation(value = "查询项目风险", httpMethod = "GET")
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
        log.info("分页查询风险内容信息");
        Page<ProjectRisk> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        orderItems.clear();
        // 这里排序使用的字段名称对应数据库的字段名称
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        QueryWrapper<ProjectRisk> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(project_number),"project_number", project_number);
        IPage<ProjectRisk> mapIPage;
        try {
            mapIPage = projectRiskService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "分页服务记录", mapIPage));
    }

    @ApiOperation(value = "添加项目风险", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ProjectRisk projectRisk) {
        log.info("添加项目风险信息");
        try {
            if (StringUtils.isBlank(projectRisk.getDescr())) {
                return ResponseEntity.ok().body(new Response(false, "项目风险描述不能为空！"));
            }
            
            if (StringUtils.isBlank(projectRisk.getDescr())) {
                if (projectRisk.getDescr().length()>200/3) {
                    return ResponseEntity.ok().body(new Response(false, "项目风险描述不能超过200个字符！"));
                }
            }
            if(StringUtils.isNotBlank(projectRisk.getSolution())){
                if (projectRisk.getSolution().length()>200/3) {
                    return ResponseEntity.ok().body(new Response(false, "项目风险应对方法不能超过200个字符！"));
                }
            }
            if (StringUtils.isBlank(projectService.whetherExists(projectRisk.getProjectNumber()))) {
                return ResponseEntity.ok().body(new Response(false, "项目编号为空或者不存在！", projectRisk));
            }
            projectRiskService.saveProjectRisk(projectRisk);
           // projectService.icsCount("risk_count",projectRisk.getProjectNumber());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存项目风险成功！", projectRisk));
    }

    @ApiOperation(value = "更新项目风险", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody ProjectRisk projectRisk) {
        log.info("更新项目风险信息");
        try {
            if(StringUtils.isNotBlank(projectRisk.getDescr())){
                if (projectRisk.getSolution().length() > 200/3) {
                    return ResponseEntity.ok().body(new Response(false, "项目风险解决方案不能超过200个字符！"));
                }
            }
            projectRiskService.updateById(projectRisk);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存项目风险成功！", projectRisk));
    }

    @ApiOperation(value = "删除风险", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Long> ids) {
        log.info("删除项目风险信息");
        try {
            projectRiskService.deleteRiskByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除项目风险信息成功！", ids));
    }
}

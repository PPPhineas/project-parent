package com.hgt.project.web.controller;


import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Qualification;
import com.hgt.project.dao.entity.vo.QualificationConditionVo;
import com.hgt.project.service.IQualificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 资质 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2019-12-24
 */
@RestController
@RequestMapping("/project/qualification")
@Slf4j
@Api(tags = "资质管理")
public class QualificationController {

	private final IQualificationService qualificationService;

    public QualificationController(IQualificationService qualificationService) {
        this.qualificationService = qualificationService;
    }

    @ApiOperation(value = "资质信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestBody QualificationConditionVo conditionVo) {
        log.info("分页查询资质信息");
        Page<Qualification> page = new Page<>();
        page.setCurrent(conditionVo.getCurrent());
        page.setSize(conditionVo.getSize());
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Qualification> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getCertificateName()),"certificate_name", conditionVo.getCertificateName());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getCertificateNumber()),"certificate_number", conditionVo.getCertificateNumber());
        wrapper.eq(StringUtils.isNotBlank(conditionVo.getCertificateType()),"certificate_type", conditionVo.getCertificateType());
        IPage<Qualification> mapIPage;
        try {
            mapIPage = qualificationService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "资质分页服务记录", mapIPage));
    }

    @ApiOperation(value = "添加资质信息", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Qualification qualification) {
        log.info("添加资质信息");
        try {
        	qualificationService.save(qualification);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存资质信息成功！", qualification));
    }

    @ApiOperation(value = "更新资质信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody Qualification qualification) {
        log.info("更新资质信息");
        try {
        	qualificationService.updateById(qualification);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新资质信息成功！", qualification));
    }

    @ApiOperation(value = "删除资质信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除资质信息");
        try {
        	qualificationService.removeByIds(ids);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除资质信息成功！", ids));
    }
}

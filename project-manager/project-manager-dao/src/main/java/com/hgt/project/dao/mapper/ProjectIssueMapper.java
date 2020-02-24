package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.ProjectIssue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.baomidou.mybatisplus.core.assist.ISqlRunner.UPDATE;

/**
 * <p>
 * 项目问题 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface ProjectIssueMapper extends BaseMapper<ProjectIssue> {
    List<Map<String, Long>> queryProjectIssueCount(Set<Long> ids);
    List<Map<String,Long>>sumIssue(@Param("pNum") String pNum);
    void updateProjectIssueCount(Map<String, Long> param);
}
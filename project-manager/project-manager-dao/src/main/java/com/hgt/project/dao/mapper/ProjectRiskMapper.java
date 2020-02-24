package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.ProjectRisk;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 项目风险 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface ProjectRiskMapper extends BaseMapper<ProjectRisk> {
    List<Map<String,Long>> findProjectNumberById(Set<Long> ids);
    List<Map<String,Long>>sumRisk(@Param("pNum") String pNum);

    void updateRiskCount(Map<String, Long> param);
}

package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.Project;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface ProjectMapper extends BaseMapper<Project> {
        void icsCount(@Param("field") String field,
                      @Param("pNum") String pNum);
        String whetherExists(@Param("pNum")String pNum);
}

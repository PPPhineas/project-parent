package com.hgt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.ProjectIssue;
import com.hgt.project.dao.entity.ProjectRisk;

import java.util.Set;

/**
 * <p>
 * 项目风险 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IProjectRiskService extends IService<ProjectRisk> {

    boolean deleteRiskByIds(Set<Long> ids);
    boolean saveProjectRisk(ProjectRisk projectRisk);

}

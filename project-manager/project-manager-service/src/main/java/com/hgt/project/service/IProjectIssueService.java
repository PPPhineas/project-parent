package com.hgt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.ProjectIssue;

import java.util.Set;

/**
 * <p>
 * 项目问题 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IProjectIssueService extends IService<ProjectIssue> {

    boolean deleteIssueByIds(Set<Long> ids);

    boolean saveProjectIssue(ProjectIssue projectIssue);
}
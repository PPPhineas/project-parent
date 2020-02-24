package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProjectMilestone;
import com.hgt.project.dao.mapper.ProjectMilestoneMapper;
import com.hgt.project.service.IProjectMilestoneService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目里程碑 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProjectMilestoneServiceImpl extends ServiceImpl<ProjectMilestoneMapper, ProjectMilestone> implements IProjectMilestoneService {

}

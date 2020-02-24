package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProjectEmployee;
import com.hgt.project.dao.mapper.ProjectEmployeeMapper;
import com.hgt.project.service.IProjectEmployeeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目参与人 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProjectEmployeeServiceImpl extends ServiceImpl<ProjectEmployeeMapper, ProjectEmployee> implements IProjectEmployeeService {

}

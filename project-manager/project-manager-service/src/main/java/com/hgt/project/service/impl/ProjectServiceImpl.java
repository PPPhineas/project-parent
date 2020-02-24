package com.hgt.project.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.mapper.ProjectMapper;
import com.hgt.project.dao.util.ExcelUtil;
import com.hgt.project.service.IProjectService;

/**
 * <p>
 * 项目 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {
    @Autowired
    public ProjectMapper projectMapper;
    private ExcelUtil util = new ExcelUtil();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Project> importProjectInfo(MultipartFile file, Integer type) throws Exception {
        // 1.获取导入项目信息列表
        List<Project> projectList = util.importProjects(file, type);
        // 2.查询是否有项目编号已经存在的项目
        List<Project> localProjectList = list();
        List<String> numbers = localProjectList.stream().map(Project::getProjectNumber).collect(Collectors.toList());
        // 3.将本地项目编号与导入的项目信息中的项目编号做对比
        List<Project> resultList = projectList.stream().filter(
                m -> numbers.contains(m.getProjectName())
        ).collect(Collectors.toList());
        // 4.批量插入项目编号不同的项目
        saveBatch(resultList);
        return resultList;
    }
    
    public void icsCount(String field,String pNum){
        projectMapper.icsCount(field,pNum);
    }
    public String whetherExists(String pNum){return projectMapper.whetherExists(pNum);}
}

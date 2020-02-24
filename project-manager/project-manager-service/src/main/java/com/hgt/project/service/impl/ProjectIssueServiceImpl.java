package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProjectIssue;
import com.hgt.project.dao.mapper.ProjectIssueMapper;
import com.hgt.project.dao.mapper.ProjectMapper;
import com.hgt.project.service.IProjectIssueService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 项目问题 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProjectIssueServiceImpl extends ServiceImpl<ProjectIssueMapper, ProjectIssue> implements IProjectIssueService {
    @Autowired
    public ProjectIssueMapper projectIssueMapper;
        /**
     * 删除项目问题，并更新项目问题数量
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public boolean deleteIssueByIds(Set<Long> ids) {
        boolean result = this.removeByIds(ids);
        if (result) {
            List<Map<String, Long>> projIssueCountList = projectIssueMapper.queryProjectIssueCount(ids);
            if (CollectionUtils.isNotEmpty(projIssueCountList)) {
                for (Map<String, Long> param : projIssueCountList) {
                    projectIssueMapper.updateProjectIssueCount(param);
                }
            }
        }
        return result;
    }
    /**
     * 添加项目问题，并更新项目问题数量
     * @param projectIssue
     * @return
     */
    @Transactional
    @Override
    public boolean saveProjectIssue(ProjectIssue projectIssue){
        boolean result = this.save(projectIssue);
        if(result){
            List<Map<String,Long>> projectIssueCount=projectIssueMapper.sumIssue(projectIssue.getProjectNumber());
            if (CollectionUtils.isNotEmpty(projectIssueCount)){
                for(Map<String,Long> param:projectIssueCount){
                    param.put("issueCount",-1L);
                    projectIssueMapper.updateProjectIssueCount(param);
                }
            }
        }
        return result;
    }
}
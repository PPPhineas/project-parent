package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProjectRisk;
import com.hgt.project.dao.entity.ProjectRisk;
import com.hgt.project.dao.mapper.ProjectRiskMapper;
import com.hgt.project.service.IProjectRiskService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 项目风险 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */

@Service
public class ProjectRiskServiceImpl extends ServiceImpl<ProjectRiskMapper, ProjectRisk> implements IProjectRiskService {
    @Autowired
    ProjectRiskMapper projectRiskMapper;
    @Transactional
    @Override
    public boolean deleteRiskByIds(Set<Long> ids){
        boolean result = this.removeByIds(ids);
        if (result){
            List<Map<String,Long>> projectNumCount=projectRiskMapper.findProjectNumberById(ids);
            if (CollectionUtils.isNotEmpty(projectNumCount)){
                for (Map<String,Long> param:projectNumCount){
                    projectRiskMapper.updateRiskCount(param);
                }
            }
         }
        return result;
    }
    public boolean saveProjectRisk(ProjectRisk projectRisk){
        boolean result = this.save(projectRisk);
        if(result){
            List<Map<String,Long>> projectRiskCount=projectRiskMapper.sumRisk(projectRisk.getProjectNumber());
            if (CollectionUtils.isNotEmpty(projectRiskCount)){
                for(Map<String,Long> param:projectRiskCount){
                    param.put("proNum_count",-1L);
                    projectRiskMapper.updateRiskCount(param);
                }
            }
        }
        return result;
    }

}

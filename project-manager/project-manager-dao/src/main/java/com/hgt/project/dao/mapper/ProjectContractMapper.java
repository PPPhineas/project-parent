package com.hgt.project.dao.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.dao.entity.ProjectContract;
import com.hgt.project.dao.entity.vo.ProjectContractVo;

/**
 * <p>
 * 项目合同信息 Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface ProjectContractMapper extends BaseMapper<ProjectContract> {

	/**
	 * 分页获取项目合同信息
	 * @param page
	 * @param wrapper
	 * @return
	 */
	IPage<ProjectContractVo> pageProjectContract(Page<ProjectContractVo> page, QueryWrapper<ProjectContractVo> wrapper);

	List<ProjectContractVo> listProjectContract(QueryWrapper<ProjectContractVo> wrapper);
	
	Integer updateAmountByContractNumber(Map<String, Object> param);

}

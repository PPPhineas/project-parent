package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProjectContract;
import com.hgt.project.dao.entity.vo.ProjectContractVo;
import com.hgt.project.dao.mapper.ProjectContractMapper;
import com.hgt.project.service.IProjectContractService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 项目合同信息 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProjectContractServiceImpl extends ServiceImpl<ProjectContractMapper, ProjectContract> implements IProjectContractService {
	@Autowired
	private ProjectContractMapper projectContractMapper;
	/**
	 * 分页获取项目合同信息
	 * @param page
	 * @param wrapper
	 * @return
	 */
	@Override
	public IPage<ProjectContractVo> pageProjectContract(Page<ProjectContractVo> page,
			QueryWrapper<ProjectContractVo> wrapper) {
		return projectContractMapper.pageProjectContract(page, wrapper);
	}
	
	@Override
	public List<ProjectContractVo> listProjectContract(QueryWrapper<ProjectContractVo> wrapper) {
		return projectContractMapper.listProjectContract(wrapper);
	}
	
	/**
	 * 更新回款/支付金额
	 * @param contractNumber
	 * @param fundAmount
	 */
	@Override
	public Integer updateAmountByContractNumber(String contractNumber, Float fundAmount) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("contractNumber", contractNumber);
		param.put("fundAmount", fundAmount);
		return projectContractMapper.updateAmountByContractNumber(param);
	}

	
}

package com.hgt.project.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.ProjectContract;
import com.hgt.project.dao.entity.vo.ProjectContractVo;

/**
 * <p>
 * 项目合同信息 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IProjectContractService extends IService<ProjectContract> {
	
	/**
	 * 分页获取项目合同信息
	 * @param page
	 * @param wrapper
	 * @return
	 */
	IPage<ProjectContractVo> pageProjectContract(Page<ProjectContractVo> page, QueryWrapper<ProjectContractVo> wrapper);

	List<ProjectContractVo> listProjectContract(QueryWrapper<ProjectContractVo> wrapper);
	/**
	 * 更新回款/支付金额
	 * @param contractNumber
	 * @param fundAmount
	 */
	Integer updateAmountByContractNumber(String contractNumber, Float fundAmount);

}

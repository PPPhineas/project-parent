package com.hgt.project.dao.entity.vo;

import java.io.Serializable;
import java.util.List;

import com.hgt.project.dao.entity.Contract;
import com.hgt.project.dao.entity.PaymentCollectionDetails;
import com.hgt.project.dao.entity.ProjectContract;

/**
 * 项目合同信息
 * @author tianguifang
 *
 */
public class ProjectContractVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5359369596495951138L;
	// 合同信息
	private Contract contract;
	// 项目合同关联信息
	private ProjectContract pc;
	// 支付明细
	private List<PaymentCollectionDetails> payDetails;
	
	public List<PaymentCollectionDetails> getPayDetails() {
		return payDetails;
	}
	public void setPayDetails(List<PaymentCollectionDetails> payDetails) {
		this.payDetails = payDetails;
	}
	public Contract getContract() {
		return contract;
	}
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	public ProjectContract getPc() {
		return pc;
	}
	public void setPc(ProjectContract pc) {
		this.pc = pc;
	}
	
	
}

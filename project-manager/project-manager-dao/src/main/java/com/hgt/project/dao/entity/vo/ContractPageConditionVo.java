package com.hgt.project.dao.entity.vo;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

public class ContractPageConditionVo extends PagerVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6776942887676597133L;

	//@ApiModelProperty(value = "合同编号")
    private String contractNumber;

    //@ApiModelProperty(value = "合同名称")
    private String contractName;

    //@ApiModelProperty(value = "合同类型（1：测绘，2：信息化，3：其他）")
    private String contractType;

    //@ApiModelProperty(value = "合同分类（inflow：流入，outflow：流出，agreement:协议）")
    private String contractCategory;
    
    //@ApiModelProperty(value = "签订时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signTimeStart;
    
    //@ApiModelProperty(value = "签订时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signTimeEnd;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractCategory() {
		return contractCategory;
	}

	public void setContractCategory(String contractCategory) {
		this.contractCategory = contractCategory;
	}

	public LocalDateTime getSignTimeStart() {
		return signTimeStart;
	}

	public void setSignTimeStart(LocalDateTime signTimeStart) {
		this.signTimeStart = signTimeStart;
	}

	public LocalDateTime getSignTimeEnd() {
		return signTimeEnd;
	}

	public void setSignTimeEnd(LocalDateTime signTimeEnd) {
		this.signTimeEnd = signTimeEnd;
	}

}

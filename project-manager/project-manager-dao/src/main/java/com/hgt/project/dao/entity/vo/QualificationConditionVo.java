package com.hgt.project.dao.entity.vo;

public class QualificationConditionVo extends PagerVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1808953636950403387L;
	//@ApiModelProperty(value = "资质名称")
    private String certificateName;

    //@ApiModelProperty(value = "证书类型（0资格类，1行业类，2运维类）")
    private String certificateType;

    //@ApiModelProperty(value = "证书编号")
    private String certificateNumber;

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
    
    
}

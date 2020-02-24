package com.hgt.project.dao.entity.vo;

/**
 * 投标信息分页查询条件
 * @author tianguifang
 *
 */
public class BidPageConditionVo extends PagerVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4639109194401895014L;

    //@ApiModelProperty(value = "名称")
    private String bidName;

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
    
    
}

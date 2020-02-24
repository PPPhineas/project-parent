package com.hgt.project.dao.entity.vo;

import java.io.Serializable;

public class PagerVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8561117153834445476L;
	/**
	 * 每页显示条数
	 */
	private long size = 10;
	/**
	 * 当前页数
	 */
	private long current = 1;
	
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getCurrent() {
		return current;
	}
	public void setCurrent(long current) {
		this.current = current;
	}

	
}

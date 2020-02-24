package com.hgt.project.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目合同信息
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProjectContract对象", description="项目合同信息")
public class ProjectContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    @ApiModelProperty(value = "项目编号")
    private String projectNumber;

    @ApiModelProperty(value = "开票总金额")
    private Float invoiceAmount;

    @ApiModelProperty(value = "回款/付款额")
    private Float returnedAmount;

    @ApiModelProperty(value = "待回款/付款额")
    private Float pendingAmount;

    @ApiModelProperty(value = "合同完成时间")
    private LocalDateTime completedTime;

    @ApiModelProperty(value = "0:初始状态  1：部分付款/回款   2：全部付款/回款")
    private Integer contractStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

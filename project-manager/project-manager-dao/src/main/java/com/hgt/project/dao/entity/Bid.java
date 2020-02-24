package com.hgt.project.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 投标信息
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Bid对象", description="投标信息")
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "投标负责部门编号")
    private String departmentCode;

    @ApiModelProperty(value = "名称")
    private String bidName;

    @ApiModelProperty(value = "标段")
    private String section;

    @ApiModelProperty(value = "预算金额（万元）")
    private Float budget;

    @ApiModelProperty(value = "采购单位")
    private String procurementUnit;

    @ApiModelProperty(value = "投标性质")
    private String sign;

    @ApiModelProperty(value = "申请人")
    private String proposer;

    @ApiModelProperty(value = "协调人")
    private String coordinator;

    @ApiModelProperty(value = "项目报名人")
    private String projectApply;

    @ApiModelProperty(value = "标书编制人")
    private String bidEditor;

    @ApiModelProperty(value = "现场投标人")
    private String bidor;

    @ApiModelProperty(value = "合作单位")
    private String cooperator;

    @ApiModelProperty(value = "保证金额（万元）")
    private Float cashDeposit;

    @ApiModelProperty(value = "开标时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bidOpeningTime;

    @ApiModelProperty(value = "中标单位")
    private String bidUnit;

    @ApiModelProperty(value = "中标金额")
    private Float bidPrice;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "投标申请单是否提交")
    private Integer taskSubmit;

    @ApiModelProperty(value = "投标申请单编号")
    private String taskNumber;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

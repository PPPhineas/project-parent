package com.hgt.project.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Contract对象", description="合同")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "合同类型（1：测绘，2：信息化，0：其他）")
    private Integer contractType;

    @ApiModelProperty(value = "合同分类（inflow：流入，outflow：流出，agreement:协议）")
    private String contractCategory;

    @ApiModelProperty(value = "合同对方单位名称")
    private String unit;

    @ApiModelProperty(value = "合同标的")
    private String contractObject;

    @ApiModelProperty(value = "部门id")
    private String departmentCode;

    @ApiModelProperty(value = "签订时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signTime;

    @ApiModelProperty(value = "合同金额（单位：万元）")
    private Float contractAmount;

    @ApiModelProperty(value = "是否原件")
    private Integer isOriginal;

    @ApiModelProperty(value = "是否验收文件")
    private Integer isCheckFile;

    @ApiModelProperty(value = "是否质检报告")
    private Integer isQualityReport;

    @ApiModelProperty(value = "合同份数")
    private Integer contractCount;

    @ApiModelProperty(value = "接收时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @ApiModelProperty(value = "备注")
    private String remark;


    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

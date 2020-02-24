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
 * 合同汇款付款明细表
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PaymentCollectionDetails对象", description="合同汇款付款明细表")
public class PaymentCollectionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

    @ApiModelProperty(value = "0回款, 1汇款")
    private String fundType;

    @ApiModelProperty(value = "回款汇款金额")
    private Float fundAmount;

    @ApiModelProperty(value = "回款汇款时间")
    private LocalDateTime fundTime;

    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

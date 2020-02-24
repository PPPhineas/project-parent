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
 * 项目里程碑
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProjectMilestone对象", description="项目里程碑")
public class ProjectMilestone implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目编号")
    private String projectNumber;

    @ApiModelProperty(value = "重大里程碑事件")
    private String event;

    @ApiModelProperty(value = "计划完成时间")
    private LocalDateTime plannedTime;

    @ApiModelProperty(value = "百分比")
    private Float percentage;

    @ApiModelProperty(value = "实际完成时间")
    private LocalDateTime actualTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

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
 * 项目
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Project对象", description="项目")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "项目所在部门id")
    private String departmentCode;

    @ApiModelProperty(value = "项目编号")
    private String projectNumber;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "项目背景")
    private String projectDescribe;

    @ApiModelProperty(value = "项目经理")
    private String principal;

    @ApiModelProperty(value = "项目所处阶段")
    private String phase;

    @ApiModelProperty(value = "项目整体进度")
    private String schedule;

    @ApiModelProperty(value = "进度偏差")
    private String progressBar;

    @ApiModelProperty(value = "立项时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectTime;

    @ApiModelProperty(value = "项目问题总数")
    private Integer issueCount;

    @ApiModelProperty(value = "项目风险总数")
    private Integer riskCount;

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

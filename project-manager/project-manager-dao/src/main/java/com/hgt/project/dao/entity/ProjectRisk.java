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
 * 项目风险
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProjectRisk对象", description="项目风险")
public class ProjectRisk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "风险编号")
    private String riskNumber;

    @ApiModelProperty(value = "项目编号")
    private String projectNumber;

    @ApiModelProperty(value = "提出人")
    private Long exhibitor;

    @ApiModelProperty(value = "发现时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime presentation;

    @ApiModelProperty(value = "风险描述")
    private String descr;

    @ApiModelProperty(value = "风险程度（0：轻度，1：中等，2：严重）")
    private Integer lev;

    @ApiModelProperty(value = "应对方法")
    private String solution;

    @ApiModelProperty(value = "负责人")
    private Long principal;

    @ApiModelProperty(value = "实际执行措施情况")
    private String cond;

    @ApiModelProperty(value = "状态（0：新建，1：处理中，2：关闭）")
    private Integer state;

    private String createUser;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String updateUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;


}

package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.DateUtils;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Contract;
import com.hgt.project.dao.entity.PaymentCollectionDetails;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectContract;
import com.hgt.project.dao.entity.vo.ProjectContractConditionVo;
import com.hgt.project.dao.entity.vo.ProjectContractVo;
import com.hgt.project.service.IContractService;
import com.hgt.project.service.IPaymentCollectionDetailsService;
import com.hgt.project.service.IProjectContractService;
import com.hgt.project.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 项目合同信息 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2020-01-02
 */
@RestController
@RequestMapping("/project/project-contract")
@Slf4j
@Api(tags = "项目管理--项目合同管理")
public class ProjectContractController {

	private final IProjectContractService projectContractService;
	private final IContractService contractService;
	private final IProjectService projectService;
	private final IPaymentCollectionDetailsService paymentCollectionDetailsService;

    public ProjectContractController(IProjectContractService projectContractService, IContractService contractService, IProjectService projectService, IPaymentCollectionDetailsService paymentCollectionDetailsService) {
        this.projectContractService = projectContractService;
        this.contractService = contractService;
        this.projectService = projectService;
        this.paymentCollectionDetailsService = paymentCollectionDetailsService;
    }

    @ApiOperation(value = "项目合同信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestBody ProjectContractConditionVo conditionVo) {
        log.info("分页查询项目合同信息");
        Page<ProjectContractVo> page = new Page<>();
        page.setCurrent(conditionVo.getCurrent());
        page.setSize(conditionVo.getSize());
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<ProjectContractVo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("pc.create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractNumber()),"c.contract_number", conditionVo.getContractNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractName()),"c.contract_name", conditionVo.getContractName());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractCategory())),"c.contract_category", conditionVo.getContractCategory());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractType())),"c.contract_type", conditionVo.getContractType());
        wrapper.between((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() != null), "c.sign_time", conditionVo.getSignTimeStart(), conditionVo.getSignTimeEnd());
        wrapper.lt((conditionVo.getSignTimeStart() == null && conditionVo.getSignTimeEnd() != null), "c.sign_time", conditionVo.getSignTimeEnd());
        wrapper.gt((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() == null), "c.sign_time", conditionVo.getSignTimeStart());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getProjectNumber()),"pc.project_number", conditionVo.getProjectNumber());
        IPage<ProjectContractVo> mapIPage;
        try {
            mapIPage = projectContractService.pageProjectContract(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "合同分页服务记录", mapIPage));
    }

	@ApiOperation(value = "项目合同信息导出", httpMethod = "GET")
    @GetMapping(path = "/export")
    public void export(ProjectContractConditionVo conditionVo, HttpServletResponse response) {
        log.info("导出项目合同信息");
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<ProjectContractVo> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("pc.create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractNumber()),"c.contract_number", conditionVo.getContractNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractName()),"c.contract_name", conditionVo.getContractName());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractCategory())),"c.contract_category", conditionVo.getContractCategory());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractType())),"c.contract_type", conditionVo.getContractType());
        wrapper.between((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() != null), "c.sign_time", conditionVo.getSignTimeStart(), conditionVo.getSignTimeEnd());
        wrapper.lt((conditionVo.getSignTimeStart() == null && conditionVo.getSignTimeEnd() != null), "c.sign_time", conditionVo.getSignTimeEnd());
        wrapper.gt((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() == null), "c.sign_time", conditionVo.getSignTimeStart());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getProjectNumber()),"pc.project_number", conditionVo.getProjectNumber());
        List<ProjectContractVo> list;
        try {
            list = projectContractService.listProjectContract(wrapper);
            
            // 设置响应头
    		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    		// 执行文件写入
    		response.setHeader("Content-Disposition", "attachment;filename=" + new String(("合同信息表" + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    		// 获取输出流
    		OutputStream outputStream = response.getOutputStream();
    		// 定义Excel对象
    		// 定义Excel对象
            WritableWorkbook book = Workbook.createWorkbook(outputStream);
            // 创建Sheet页
            WritableSheet sheet = book.createSheet("数据", 0);
            // 冻结表头
            SheetSettings settings = sheet.getSettings();
            settings.setVerticalFreeze(1);
            // 定义表头字体样式、表格字体样式
            WritableFont headerFont = new WritableFont(
                    WritableFont.createFont("Lucida Grande"), 9, WritableFont.BOLD);
            WritableFont bodyFont = new WritableFont(
                    WritableFont.createFont("Lucida Grande"), 9,
                    WritableFont.NO_BOLD);
            WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
            WritableCellFormat bodyCellFormat = new WritableCellFormat(bodyFont);
            // 设置表头样式：加边框、背景颜色为淡灰、居中样式
            headerCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            headerCellFormat.setBackground(Colour.PALE_BLUE);
            headerCellFormat.setAlignment(Alignment.CENTRE);
            headerCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 设置表格体样式：加边框、居中
            bodyCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            bodyCellFormat.setAlignment(Alignment.CENTRE);
            bodyCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            // 循环写入表头
            int seq = 0;
            String[] titles = {"合同编号", "项目编号", "合同名称", "合同类别", "合同对方单位名称", "资金流向 ", "合同签订时间", "合同份数（PMO存档）", "部门",  "合同金额（单位：万元 ）", 
            		"开票金额", "回款/付款金额", "待回款/付款金额", "合同完成时间", "备注"};
            for (String v: titles) {
                sheet.addCell(new Label(seq, 0, v, headerCellFormat));
                seq++;
            }
            // 判断表中是否有数据
            int line = 1;
            if (Objects.nonNull(list)) {
	            for (ProjectContractVo obj: list) {
	                seq = 0;
	                sheet.addCell(new Label(seq++, line, obj.getContract().getContractNumber(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getPc().getProjectNumber(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContract().getContractName(), bodyCellFormat));
	                // 合同类型
	                String typeStr;
	                if (obj.getContract().getContractType() == 1) {
	                	typeStr = "测绘";
	                } else if (obj.getContract().getContractType() == 2) {
	                	typeStr = "信息化";
	                } else {
	                	typeStr = "其他";
	                }
	                sheet.addCell(new Label(seq++, line, typeStr, bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContract().getUnit(), bodyCellFormat));
	                // 金额流向 （inflow：流入，outflow：流出，agreement:协议）
	                String flowStr = "";
                    switch (obj.getContract().getContractCategory()) {
                        case "inflow":
                            flowStr = "流入";
                            break;
                        case "outflow":
                            flowStr = "流出";
                            break;
                        case "agreement":
                            flowStr = "协议";
                            break;
                    }
	                
	                sheet.addCell(new Label(seq++, line, flowStr, bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getContract().getSignTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContract().getContractCount().toString(), bodyCellFormat));
	                
	                sheet.addCell(new Label(seq++, line, obj.getContract().getDepartmentCode(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContract().getContractAmount().toString(), bodyCellFormat));
	                // 开票金额
	                sheet.addCell(new Label(seq++, line, obj.getPc().getInvoiceAmount().toString(), bodyCellFormat));
	                // 回款/付款金额
	                sheet.addCell(new Label(seq++, line, obj.getPc().getReturnedAmount().toString(), bodyCellFormat));
	                // 待回款/付款金额
	                sheet.addCell(new Label(seq++, line, obj.getPc().getPendingAmount().toString(), bodyCellFormat));
	                // 合同完成时间
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getPc().getCompletedTime(), null), bodyCellFormat));
	                // 备注
	                sheet.addCell(new Label(seq++, line, obj.getPc().getRemark(), bodyCellFormat));
                    line++;
	            }
            }
			
			// 写入Excel工作表
			book.write();
			// 关闭Excel工作薄对象
			book.close();
			// 关闭流
			outputStream.flush();
			outputStream.close();
        } catch (Exception e) {
        	log.error("导出合同信息报错：", e);
        }
    }
	
    @ApiOperation(value = "录入项目合同信息", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ProjectContract contract) {
        log.info("添加项目合同信息");
        try {
        	// 部分数据验证
        	if (StringUtils.isBlank(contract.getContractNumber())) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号不能为空"));
        	}
        	if (contract.getContractNumber().length() > 20) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号长度不能超过20个字符"));
        	}
        	// 验证合同编号是否存在
        	QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("contract_number", contract.getContractNumber());
        	List<Contract> contractList = contractService.list(queryWrapper);
        	if (CollectionUtils.isEmpty(contractList)) {
        		return ResponseEntity.ok().body(new Response(false, "该合同编号的合同不存在"));
        	}
        	
        	// 如果合同有项目，则验证项目是否正确
        	if (StringUtils.isNotBlank(contract.getProjectNumber())) {
        		QueryWrapper<Project> pqueryWrapper = new QueryWrapper<>();
        		pqueryWrapper.eq("project_number", contract.getProjectNumber());
            	List<Project> projectList = projectService.list(pqueryWrapper);
            	if (CollectionUtils.isEmpty(projectList)) {
            		return ResponseEntity.ok().body(new Response(false, "该项目编号的项目不存在"));
            	}
        	}
        	// 保存合同数据
        	projectContractService.save(contract);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存项目合同信息成功！", contract.getId()));
    }

    @ApiOperation(value = "更新项目合同信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody ProjectContract contract) {
        log.info("更新项目合同信息");
        try {
        	projectContractService.updateById(contract);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新项目合同信息成功！", contract));
    }

    @ApiOperation(value = "删除项目合同信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除项目合同信息");
        try {
        	projectContractService.removeByIds(ids);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除项目合同信息成功！", ids));
    }
    
    ///////////////////////支付明细////////////////////////
    @ApiOperation(value = "录入项目合同支付明细信息", httpMethod = "POST")
    @PostMapping("/savePayDetail")
    public ResponseEntity<Response> savePayDetail(@RequestBody PaymentCollectionDetails detail) {
        log.info("添加项目合同支付明细信息");
        try {
        	// 部分数据验证
        	if (StringUtils.isBlank(detail.getContractNumber())) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号不能为空"));
        	}
        	if (detail.getContractNumber().length() > 20) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号长度不能超过20个字符"));
        	}
        	// 验证合同编号是否存在
        	QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("contract_number", detail.getContractNumber());
        	List<Contract> contractList = contractService.list(queryWrapper);
        	if (CollectionUtils.isEmpty(contractList)) {
        		return ResponseEntity.ok().body(new Response(false, "该合同编号的合同不存在"));
        	}
        	// 更新项目合同相关金额
        	if (detail.getFundAmount() != null) {
        		// 保存合同数据
        		paymentCollectionDetailsService.save(detail);
        		// 更新合同回款或支付的金额
        		projectContractService.updateAmountByContractNumber(detail.getContractNumber(), detail.getFundAmount());
        	} else {
        		if (detail.getFundType().equals("0")) { // 回款
        			return ResponseEntity.ok().body(new Response(false, "回款金额不能不为空"));
        		} else { // 支付
        			return ResponseEntity.ok().body(new Response(false, "支付金额不能为空"));
        		}
        	}
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存项目合同支付明细信息成功！", detail.getId()));
    }
    
}

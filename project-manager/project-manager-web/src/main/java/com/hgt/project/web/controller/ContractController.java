package com.hgt.project.web.controller;


import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.DateUtils;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Contract;
import com.hgt.project.dao.entity.vo.ContractPageConditionVo;
import com.hgt.project.service.IContractService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import lombok.extern.slf4j.Slf4j;
/**
 * <p>
 * 合同 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2019-12-23
 */
@RestController
@RequestMapping("/project/contract")
@Slf4j
@Api(tags = "项目管理--合同管理")
public class ContractController {

	private final IContractService contractService;

    public ContractController(IContractService contractService) {
        this.contractService = contractService;
    }

    @ApiOperation(value = "合同信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestBody ContractPageConditionVo conditionVo) {
        log.info("分页查询合同信息");
        Page<Contract> page = new Page<>();
        page.setCurrent(conditionVo.getCurrent());
        page.setSize(conditionVo.getSize());
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractNumber()),"contract_number", conditionVo.getContractNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractName()),"contract_name", conditionVo.getContractName());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractCategory())),"contract_category", conditionVo.getContractCategory());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractType())),"contract_type", conditionVo.getContractType());
        wrapper.between((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() != null), "sign_time", conditionVo.getSignTimeStart(), conditionVo.getSignTimeEnd());
        wrapper.lt((conditionVo.getSignTimeStart() == null && conditionVo.getSignTimeEnd() != null), "sign_time", conditionVo.getSignTimeEnd());
        wrapper.gt((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() == null), "sign_time", conditionVo.getSignTimeStart());
        IPage<Contract> mapIPage;
        try {
            mapIPage = contractService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "合同分页服务记录", mapIPage));
    }

	@ApiOperation(value = "合同信息导出", httpMethod = "GET")
    @GetMapping(path = "/export")
    public void export(ContractPageConditionVo conditionVo, HttpServletResponse response) {
        log.info("导出合同信息");
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Contract> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractNumber()),"contract_number", conditionVo.getContractNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getContractName()),"contract_name", conditionVo.getContractName());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractCategory())),"contract_category", conditionVo.getContractCategory());
        wrapper.eq((StringUtils.isNotBlank(conditionVo.getContractType())),"contract_type", conditionVo.getContractType());
        wrapper.between((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() != null), "sign_time", conditionVo.getSignTimeStart(), conditionVo.getSignTimeEnd());
        wrapper.lt((conditionVo.getSignTimeStart() == null && conditionVo.getSignTimeEnd() != null), "sign_time", conditionVo.getSignTimeEnd());
        wrapper.gt((conditionVo.getSignTimeStart() != null && conditionVo.getSignTimeEnd() == null), "sign_time", conditionVo.getSignTimeStart());
        List<Contract> list;
        try {
            list = contractService.list(wrapper);
            
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
            String[] titles = {"合同编号", "合同名称", "合同类别", "合同对方单位名称", "合同标的 ", "部门", "签订时间", "合同金额（单位：万元 ）", "是否原件", "验收文件",
            		"检验报告", "接收时间", "备注"};
            for (String v: titles) {
                sheet.addCell(new Label(seq, 0, v, headerCellFormat));
                seq++;
            }
            // 判断表中是否有数据
            int line = 1;
            if (Objects.nonNull(list)) {
	            for (Contract obj: list) {
	                seq = 0;
	                sheet.addCell(new Label(seq++, line, obj.getContractNumber(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContractName(), bodyCellFormat));
	                // 合同类型
	                String typeStr = "其他";
	                if (obj.getContractType() == 1) {
	                	typeStr = "测绘";
	                } else if (obj.getContractType() == 2) {
	                	typeStr = "信息化";
	                } else {
	                	typeStr = "其他";
	                }
	                sheet.addCell(new Label(seq++, line, typeStr, bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getUnit(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContractObject(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getDepartmentCode(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getSignTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContractAmount().toString(), bodyCellFormat));
	                // 是否原价
	                String oraginalStr = "否";
	                if (obj.getIsOriginal() != null && obj.getIsOriginal() == 1) {
	                	oraginalStr = "是";
	                }
	                sheet.addCell(new Label(seq++, line, oraginalStr, bodyCellFormat));
	                // 是否是验收文件
	                String checkFileStr = "否";
	                if (obj.getIsCheckFile() != null && obj.getIsCheckFile() == 1) {
	                	checkFileStr = "是";
	                }
	                sheet.addCell(new Label(seq++, line, checkFileStr, bodyCellFormat));
	                
	                // 是否是质检报告
	                String qualityReportStr = "否";
	                if (obj.getIsQualityReport() != null && obj.getIsQualityReport() == 1) {
	                	qualityReportStr = "是";
	                }
	                sheet.addCell(new Label(seq++, line, qualityReportStr, bodyCellFormat));
	                // 接收时间
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getReceiveTime(), null), bodyCellFormat));
	                // 备注
	                sheet.addCell(new Label(seq++, line, obj.getRemark(), bodyCellFormat));
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
			outputStream = null;
        } catch (Exception e) {
        	log.error("导出合同信息报错：", e);
        }
    }
	
    @ApiOperation(value = "录入合同信息", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Contract contract) {
        log.info("添加合同信息");
        try {
        	// 部分数据验证
        	if (StringUtils.isBlank(contract.getContractNumber())) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号不能为空"));
        	}
        	if (contract.getContractNumber().length() > 20) {
        		return ResponseEntity.ok().body(new Response(false, "合同编号长度不能超过20个字符"));
        	}
        	// 验证合同编号的唯一性
        	QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("contract_number", contract.getContractNumber());
        	List<Contract> contractList = contractService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(contractList)) {
        		return ResponseEntity.ok().body(new Response(false, "该合同编号的合同已经存在"));
        	}
        	
        	if (StringUtils.isBlank(contract.getContractName())) {
        		return ResponseEntity.ok().body(new Response(false, "合同名称不能为空"));
        	}
        	if (contract.getContractName().length() > 30) {
        		return ResponseEntity.ok().body(new Response(false, "合同名称长度不能超过30个字符"));
        	}
        	if (StringUtils.isBlank(contract.getUnit())) {
        		return ResponseEntity.ok().body(new Response(false, "合同对方单位名称不能为空"));
        	}
        	if (contract.getUnit().length() > 30) {
        		return ResponseEntity.ok().body(new Response(false, "合同对方单位名称长度不能超过30个字符"));
        	}
        	
        	// 保存合同数据
        	contractService.save(contract);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存合同信息成功！", contract.getId()));
    }

    @ApiOperation(value = "更新合同信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody Contract contract) {
        log.info("更新合同信息");
        try {
        	contractService.updateById(contract);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新合同信息成功！", contract));
    }

    @ApiOperation(value = "删除合同信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除合同信息");
        try {
        	contractService.removeByIds(ids);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除合同信息成功！", ids));
    }
}

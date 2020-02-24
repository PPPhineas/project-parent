package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.DateUtils;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.ProductiveTask;
import com.hgt.project.dao.entity.vo.ProductiveTaskConditionVo;
import com.hgt.project.service.IProductiveTaskService;
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
 * 生产任务单 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2019-12-23
 */
@RestController
@RequestMapping("/project/productive-task")
@Slf4j
@Api(tags = "项目管理--生产任务单管理")
public class ProductiveTaskController {

	private final IProductiveTaskService productiveTaskService;

    public ProductiveTaskController(IProductiveTaskService productiveTaskService) {
        this.productiveTaskService = productiveTaskService;
    }

    @ApiOperation(value = "生产任务单信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestBody ProductiveTaskConditionVo conditionVo) {
        log.info("分页查询生产任务单信息");
        Page<ProductiveTask> page = new Page<>();
        page.setCurrent(conditionVo.getCurrent());
        page.setSize(conditionVo.getSize());
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<ProductiveTask> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getProjectNumber()),"project_number", conditionVo.getProjectNumber());
        IPage<ProductiveTask> mapIPage;
        try {
            mapIPage = productiveTaskService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "生产任务单分页服务记录", mapIPage));
    }
	
	@ApiOperation(value = "导出生产任务单信息", httpMethod = "GET")
    @GetMapping(path = "/export")
    public void export(ProductiveTaskConditionVo conditionVo, HttpServletResponse response) {
        log.info("导出生产任务单信息");
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<ProductiveTask> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.like(StringUtils.isNotBlank(conditionVo.getProjectNumber()),"project_number", conditionVo.getProjectNumber());
        List<ProductiveTask> list;
        try {
            list = productiveTaskService.list(wrapper);
            
            // 设置响应头
    		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    		// 执行文件写入
    		response.setHeader("Content-Disposition", "attachment;filename=" + new String(("生产任务单信息表" + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    		// 获取输出流
    		OutputStream outputStream = response.getOutputStream();
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
            String[] titles = {"项目名称", "项目编号", "下达时间", "所属部门", "项目经理", "产值占比", "合同额", "产值", "合同要求完成时间 ", "项目工作内容", 
            		"立项会议时间", "立项发布时间", "验收资料", "项目结项时间", "备注"};
            for (String v: titles) {
                sheet.addCell(new Label(seq, 0, v, headerCellFormat));
                seq++;
            }
            // 判断表中是否有数据
            int line = 1;
            if (Objects.nonNull(list)) {
	            for (ProductiveTask obj: list) {
	                seq = 0;
	                sheet.addCell(new Label(seq++, line, obj.getProjectName(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getProjectNumber(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getTransmitTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getDepartmentCode(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getProjectManager(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getOutputRatio() + "%", bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getContractValue().toString(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getOutput().toString(), bodyCellFormat));
	                // 合同要求完成时间
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getDeadline(), null), bodyCellFormat));
	                // 项目工作内容
	                sheet.addCell(new Label(seq++, line, obj.getDescription(), bodyCellFormat));
	                
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getApprovalTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getPublishTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getMaterial(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getEndTime(), null), bodyCellFormat));
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
        } catch (Exception e) {
        	log.error("导出生产任务单信息", e);
        }
    }

    @ApiOperation(value = "录入生产任务单信息", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ProductiveTask productiveTask) {
        log.info("添加生产任务单信息");
        try {
        	productiveTaskService.save(productiveTask);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存生产任务单信息成功！", productiveTask));
    }

    @ApiOperation(value = "更新生产任务单信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody ProductiveTask productiveTask) {
        log.info("更新生产任务单信息");
        try {
        	productiveTaskService.updateById(productiveTask);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新生产任务单信息成功！", productiveTask));
    }

    @ApiOperation(value = "删除生产任务单信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除生产任务单信息");
        try {
        	productiveTaskService.removeByIds(ids);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除生产任务单信息成功！", ids));
    }
}

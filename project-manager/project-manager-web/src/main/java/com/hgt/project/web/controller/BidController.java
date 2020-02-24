package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.util.DateUtils;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.Bid;
import com.hgt.project.dao.entity.vo.BidPageConditionVo;
import com.hgt.project.service.IBidService;
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
 * 投标信息 前端控制器
 * </p>
 *
 * @author tianguifang
 * @since 2019-12-23
 */
@RestController
@RequestMapping("/project/bid")
@Slf4j
@Api(tags = "项目管理--投标管理")
public class BidController {

	private final IBidService bidService;

    public BidController(IBidService bidService) {
        this.bidService = bidService;
    }

    @ApiOperation(value = "投标信息列表", httpMethod = "POST")
    @PostMapping(path = "/page")
    public ResponseEntity<Response> list(@RequestBody BidPageConditionVo conditionVo) {
        log.info("分页查询投标信息");
        Page<Bid> page = new Page<>();
        page.setCurrent(conditionVo.getCurrent());
        page.setSize(conditionVo.getSize());
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Bid> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        //wrapper.like(StringUtils.isNotBlank(conditionVo.getBidNumber()),"bid_number", conditionVo.getBidNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getBidName()),"bid_name", conditionVo.getBidName());
        IPage<Bid> mapIPage;
        try {
            mapIPage = bidService.page(page, wrapper);
        } catch (Exception e) {
        	log.error("查询投标信息报错：", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "投标分页服务记录", mapIPage));
    }
	@ApiOperation(value = "投标信息导出", httpMethod = "GET")
    @GetMapping(path = "/export")
    public void export(BidPageConditionVo conditionVo, HttpServletResponse response) {
        log.info("导出查询投标信息");
        // 这里排序使用的字段名称对应数据库的字段名称
        QueryWrapper<Bid> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        //wrapper.like(StringUtils.isNotBlank(conditionVo.getBidNumber()),"bid_number", conditionVo.getBidNumber());
        wrapper.like(StringUtils.isNotBlank(conditionVo.getBidName()),"bid_name", conditionVo.getBidName());
        List<Bid> list;
        try {
        	list = bidService.list(wrapper);
        	
        	// 设置响应头
    		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    		// 执行文件写入
    		response.setHeader("Content-Disposition", "attachment;filename=" + new String(("投标信息表" + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
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
            String[] titles = {"名称", "标段", "预算金额（单位：万元）", "采购单位", "投标性质", "申请人", "协调人", "投标负责部门", "项目报名人", "标书编制人",
            		"现场投标人", "合作单位", "保证金额（单位：万元）", "开标时间", "中标单位", "中标金额", "备注", "投标申请单是否提交", "投标申请单编号"};
            for (String v: titles) {
                sheet.addCell(new Label(seq, 0, v, headerCellFormat));
                seq++;
            }
            // 判断表中是否有数据
            int line = 1;
            if (Objects.nonNull(list)) {
	            for (Bid obj: list) {
	                seq = 0;
	                sheet.addCell(new Label(seq++, line, obj.getBidName(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getSection(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getBudget().toString(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getProcurementUnit(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getSign(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getProposer(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getCoordinator(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getDepartmentCode(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getProjectApply(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getBidEditor(), bodyCellFormat));
	                
	                sheet.addCell(new Label(seq++, line, obj.getBidor(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getCooperator(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getCashDeposit().toString(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, DateUtils.dateFormat(obj.getBidOpeningTime(), null), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getBidUnit(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getBidPrice().toString(), bodyCellFormat));
	                sheet.addCell(new Label(seq++, line, obj.getRemark(), bodyCellFormat));
	                if (obj.getTaskSubmit() == 1) {
	                	sheet.addCell(new Label(seq++, line, "已提交", bodyCellFormat));
	                } else {
	                	sheet.addCell(new Label(seq++, line, "未提交", bodyCellFormat));
	                }
	                sheet.addCell(new Label(seq++, line, obj.getTaskNumber(), bodyCellFormat));
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
        	log.error("导出投标信息报错：", e);
        }
    }
	
    @ApiOperation(value = "录入投标信息", httpMethod = "POST")
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Bid bid) {
        log.info("添加投标信息");
        try {
        	if (StringUtils.isBlank(bid.getBidName())) {
        		return ResponseEntity.ok().body(new Response(false, "投标名称不能为空"));
        	}
        	if (bid.getBidName().length() > 30) {
        		return ResponseEntity.ok().body(new Response(false, "投标名称不能超过30个字符"));
        	}
        	QueryWrapper<Bid> queryWrapper = new QueryWrapper<>();
        	queryWrapper.eq("bid_name", bid.getBidName().trim());
        	List<Bid> list = bidService.list(queryWrapper);
        	if (CollectionUtils.isNotEmpty(list)) {
        		return ResponseEntity.ok().body(new Response(false, "投标名称已经存在"));
        	}
        	bid.setBidName(bid.getBidName().trim());
            bidService.save(bid);
        } catch (Exception e) {
        	log.error("保存投标信息失败", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "保存投标信息成功！", bid.getId()));
    }

    @ApiOperation(value = "更新投标信息", httpMethod = "POST")
    @PostMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody Bid bid) {
        log.info("更新投标信息");
        try {
        	bidService.updateById(bid);
        } catch (Exception e) {
        	log.error("更新投标信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "更新投标信息成功！", bid));
    }

    @ApiOperation(value = "删除投标信息", httpMethod = "DELETE")
    @DeleteMapping("/delete")
    public ResponseEntity<Response> delete(@RequestParam("ids") Set<Integer> ids) {
        log.info("删除投标信息");
        try {
        	bidService.removeByIds(ids);
        } catch (Exception e) {
        	log.error("删除投标信息报错", e);
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "删除投标信息成功！", ids));
    }
	
}

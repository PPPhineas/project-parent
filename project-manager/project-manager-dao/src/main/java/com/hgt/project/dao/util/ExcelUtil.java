package com.hgt.project.dao.util;

import com.hgt.project.common.constant.ProjectConstant;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectData;
import jxl.SheetSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Data
public class ExcelUtil {

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void exportProjects(HttpServletResponse response, List<Project> list) throws IOException, WriteException {
        export(response, list);
    }

    public static void exportProjectData(HttpServletResponse response, List<ProjectData> list) {
        exportData(response, list);
    }

    public List<Project> importProjects(MultipartFile file, Integer importType) throws Exception {
        List<Project> projectList = new ArrayList<>();
        switch (importType) {
            case 0:
                // 模板导入
                projectList = importForTemplate(file);
                break;
            case 1:
                // 数据转移导入
                projectList = importForData(file);
                break;
        }
        return projectList;
    }

    private static void export(HttpServletResponse response, List<Project> list) throws IOException, WriteException {
        String fileName = URLEncoder.encode("项目信息" + LocalDateTime.now(), StandardCharsets.UTF_8.name());
        response.reset();
        // 设置响应头
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        // 执行文件写入
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        // 获取输出流
        OutputStream outputStream = response.getOutputStream();
        // 定义Excel对象
        WritableWorkbook book = jxl.Workbook.createWorkbook(outputStream);
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
        for (String v: ProjectConstant.TITLE) {
            sheet.addCell(new Label(seq, 0, v, headerCellFormat));
            seq++;
        }
        // 判断表中是否有数据
        int line = 1;
        if (Objects.nonNull(list))
        for (Project obj: list) {
            seq = 0;
            sheet.addCell(new Label(seq++, line, obj.getProjectName(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getProjectNumber(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getProjectType(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getProjectDescribe(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getPrincipal(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getPhase(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getSchedule(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getProgressBar(), bodyCellFormat));
            if (Objects.nonNull(obj.getProjectTime()))
            sheet.addCell(new Label(seq++, line, obj.getProjectTime().format(format), bodyCellFormat));
            if (Objects.nonNull(obj.getIssueCount()))
            sheet.addCell(new Label(seq++, line, obj.getIssueCount().toString(), bodyCellFormat));
            if (Objects.nonNull(obj.getRiskCount()))
            sheet.addCell(new Label(seq++, line, obj.getRiskCount().toString(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getDepartmentCode(), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getCreateUser(), bodyCellFormat));
            if (Objects.nonNull(obj.getUpdateTime()))
            sheet.addCell(new Label(seq++, line, obj.getCreateTime().format(format), bodyCellFormat));
            sheet.addCell(new Label(seq++, line, obj.getUpdateUser(), bodyCellFormat));
            if (Objects.nonNull(obj.getUpdateTime()))
            sheet.addCell(new Label(seq, line, obj.getUpdateTime().format(format), bodyCellFormat));
            line++;
        }
        // 写入Excel工作表
        book.write();
        // 关闭Excel工作薄对象
        book.close();
        // 关闭流
        outputStream.flush();
        outputStream.close();
    }

    private static void exportData(HttpServletResponse response, List<ProjectData> list) {
        // 1. 导出数据并打包

        // 2. 导出数据清单
    }

    /**
     * 模板导入
     * @param file 上传文件
     */
    private List<Project> importForTemplate(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        assert filename != null;
        if (!filename.matches("^.+\\.(?i)(xls)$") && !filename.matches("^.+\\.(?i)(xlsx)$")) {
            throw new Exception("上传文件格式不正确");
        }
        //判断Excel文件的版本
        boolean isExcel2003 = true;
        if (filename.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        //获取Excel文件的第一页sheet，判断是否有信息
        Sheet sheet = wb.getSheetAt(0);
        if(Objects.nonNull(sheet)){
            return fillSheetToProject(sheet);
        }
        return null;
    }

    /**
     * 数据转移导入
     * @param file 上传文件
     */
    private static List<Project> importForData(MultipartFile file) {
        return null;
    }

    /**
     * 将sheet中的信息绑定到实体类中
     * @param sheet 表单信息
     */
    private List<Project> fillSheetToProject(Sheet sheet) {
        List<Project> projectList = new ArrayList<>();
        int totalRows = sheet.getPhysicalNumberOfRows();
        Row row;
        // 0是表头
        for (int i = 1; i < totalRows; i++) {
            row = sheet.getRow(i);
            projectList.add(fileProject(row));
        }
        return projectList;
    }

    private Project fileProject(Row row) {
        Project project = new Project();
        project.setDepartmentCode((String) formGetCellType(row.getCell(0)));
        project.setProjectNumber((String) formGetCellType(row.getCell(1)));
        project.setProjectName((String) formGetCellType(row.getCell(2)));
        project.setProjectType((String) formGetCellType(row.getCell(3)));
        project.setProjectDescribe((String) formGetCellType(row.getCell(4)));
        project.setPrincipal((String) formGetCellType(row.getCell(5)));
        project.setPhase((String) formGetCellType(row.getCell(6)));
        project.setSchedule((String) formGetCellType(row.getCell(7)));
        project.setProgressBar((String) formGetCellType(row.getCell(8)));
        if (Objects.nonNull(formGetCellType(row.getCell(9))))
        project.setProjectTime((LocalDateTime) formGetCellType(row.getCell(9)));
        if (Objects.nonNull(formGetCellType(row.getCell(10))))
        project.setIssueCount(Double.valueOf((String) formGetCellType(row.getCell(10))).intValue());
        if (Objects.nonNull(formGetCellType(row.getCell(11))))
        project.setRiskCount(Double.valueOf((String) formGetCellType(row.getCell(11))).intValue());
        project.setCreateUser((String) formGetCellType(row.getCell(12)));
        if (Objects.nonNull(formGetCellType(row.getCell(13))))
        project.setCreateTime((LocalDateTime) formGetCellType(row.getCell(13)));
        project.setUpdateUser((String) formGetCellType(row.getCell(14)));
        if (Objects.nonNull(formGetCellType(row.getCell(15))))
        project.setUpdateTime((LocalDateTime) formGetCellType(row.getCell(15)));
        project.setDeleted(0);
        // todo
        return project;
    }

    private Object formGetCellType(Cell cell) {
        Object value = null;
        try {
            if (Objects.nonNull(cell)) {
                if (cell.getCellType() == cell.CELL_TYPE_BLANK) {
                    value = "";
                } else if (cell.getCellType() == cell.CELL_TYPE_BOOLEAN) {
                    value = cell.getBooleanCellValue();
                } else if (cell.getCellType() == cell.CELL_TYPE_ERROR) {
                    value = "";
                } else if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
                    value = cell.getCellFormula();
                } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date tempValue = cell.getDateCellValue();
                        Instant instant = tempValue.toInstant();
                        ZoneId zoneId = ZoneId.systemDefault();
                        value = instant.atZone(zoneId).toLocalDateTime();
                    } else {
                        value = String.valueOf(cell.getNumericCellValue());
                        // 防止数字转换异常
                        if (value.equals("")) {
                            value = "0";
                        }
                    }
                } else if (cell.getCellType() == cell.CELL_TYPE_STRING) {
                    value = cell.getStringCellValue();
                } else {
                    value = cell.getDateCellValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

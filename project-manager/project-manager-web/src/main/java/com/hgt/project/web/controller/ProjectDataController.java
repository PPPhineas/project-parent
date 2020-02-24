package com.hgt.project.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgt.project.common.constant.ProjectDataConstant;
import com.hgt.project.common.util.Response;
import com.hgt.project.dao.entity.ProjectData;
import com.hgt.project.service.IProjectDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 项目文件 前端控制器
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/project/project-data")
@Slf4j
@Api(tags = "项目资料管理")
public class ProjectDataController {

    @Value("${file-path}")
    private String path;

    private List<OrderItem> orderItems = new ArrayList<>();

    private final IProjectDataService projectDataService;

    public ProjectDataController(IProjectDataService projectDataService) {
        this.projectDataService = projectDataService;
    }

    @ApiOperation(value = "项目文档资料信息分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", dataType = "int", value = "当前页"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "页大小"),
            @ApiImplicitParam(name = "projectName", dataType = "String", value = "项目名称")
    })
    @GetMapping(value = "/page")
    public ResponseEntity<Response> page(@RequestParam(value = "current", required = false, defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                         @RequestParam(value = "fileName", required = false, defaultValue = "") String fileName) {
        Page<ProjectData> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);
        orderItems.clear();
        // 这里排序使用的字段名称对应数据库的字段名称
        orderItems.add(new OrderItem().setColumn("update_time").setAsc(false));
        page.setOrders(orderItems);
        QueryWrapper<ProjectData> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNoneBlank(fileName),"file_name", fileName);
        // root目录
        wrapper.eq("parentId", 0);
        IPage<ProjectData> mapIPage;
        try {
            mapIPage = projectDataService.page(page, wrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "分页项目资料记录", mapIPage));
    }

    @ApiOperation(value = "通过id查询项目文档资料子目录")
    @GetMapping(value = "/child")
    public Object child(@RequestParam(name = "id") Long id) {
        log.info("通过id:{}查询子目录", id);
        List<ProjectData> projectData;
        try {
            QueryWrapper<ProjectData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", id);
            projectData = projectDataService.list(queryWrapper);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "项目资料子目录记录", projectData));
    }

    @ApiOperation(value = "新增项目资料")
    @PostMapping(path = "add")
    public Object add(ProjectData projectData) {
        log.info("新增项目资料");
        boolean result;
        try {
            result = projectDataService.saveWithFile(projectData);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "新增项目资料成功!", projectData));
        }
        return ResponseEntity.ok().body(new Response(false, "新增项目资料失败!", projectData));
    }

    @ApiOperation(value = "修改项目资料")
    @PutMapping(path = "edit")
    public Object update(ProjectData projectData) {
        log.info("修改项目资料");
        boolean result;
        try {
            result = projectDataService.updateWithFile(projectData);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "修改项目资料成功!", projectData));
        }
        return ResponseEntity.ok().body(new Response(false, "修改项目资料失败!", projectData));
    }

    @ApiOperation(value = "根据ID列表删除项目资料")
    @DeleteMapping(path = "delete")
    public Object deleteBatch(Set<Long> ids) {
        log.info("删除项目资料信息");
        boolean result;
        try {
            // 1.删除项目
            result = projectDataService.removeByIds(ids);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "删除项目资料成功!", ids));
        }
        return ResponseEntity.ok().body(new Response(false, "删除项目资料失败!", ids));
    }

    @ApiOperation(value = "上传项目数据")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<Response> batchUpload(MultipartFile file, ProjectData projectData) {
        log.info("上传项目数据");
        boolean result;
        try {
            // 1.删除项目
            result = projectDataService.upload(file, projectData);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        if (result) {
            return ResponseEntity.ok().body(new Response(true, "上传项目资料成功", projectData));
        }
        return ResponseEntity.ok().body(new Response(false, "上传项目资料失败", projectData));
    }

    @ApiOperation(value = "下载项目数据")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    @ResponseBody
    public void batchDownloadByIds(String ids, HttpServletResponse response) {
        downloadDataByIds(ids, response);
    }

    private void downloadDataByIds(String ids, HttpServletResponse response) {
        Map<String, String> fileNameMap = new HashMap<>();
        String zipName = "ProjectFiles-"+ LocalDateTime.now().toString() +".zip";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        // 设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            // 设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> pathList = new ArrayList<>();
        // 遍历
        for (String id : ids.split(",")) {
            ProjectData data = projectDataService.getById(id);
            if (Objects.nonNull(data)) {
                if (data.getFolder() == 0) {
                    // 是文件
                    getAddress(data, pathList);
                } else {
                    // 是文件夹
                    String path = projectDataService.getLocalPath(data.getId());
                    pathList.add(path);
                }
            }
        }
        DataOutputStream os = null;
        for (int i = 0; i < pathList.size(); i++) {
            // 要下载文件的路径
            File file = new File(path + "\\" + pathList.get(i) );
            try {
                //添加ZipEntry，并ZipEntry中写入文件流
                //这里，加上i是防止要下载的文件有重名的导致下载失败\
                String currentName = file.getName();
                String fileName = fileNameMap.get(currentName);
                if (fileName == null) {//记录是否重名,无就存入map中
                    fileNameMap.put(currentName, "00");
                } else {//有 证明有重复,添加个序号
                    currentName = i + currentName;
                    fileNameMap.put(currentName, "00");
                }
                Objects.requireNonNull(zipos).putNextEntry(new ZipEntry(currentName));
                os = new DataOutputStream(zipos);
                InputStream is = new FileInputStream(file);
                byte[] b = new byte[100];
                int length;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                zipos.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //关闭流
        try {
            if (os != null) {
                os.flush();
                os.close();
            }
            if (zipos != null) {
                zipos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getAddress(ProjectData projectData, List<String> result) {
        QueryWrapper<ProjectData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", projectData.getId());
        List<ProjectData> dataList = projectDataService.list(queryWrapper);
        if (Objects.nonNull(dataList)) {
            for (ProjectData data : dataList) {
                if (data.getFolder() == 1) {
                    String path = projectDataService.getLocalPath(data.getId());
                    result.add(path);
                } else {
                    result = getAddress(data, result);
                }
            }
        }
        return result;
    }


    @ApiOperation(value = "项目资料信息导出", httpMethod = "GET")
    @GetMapping(value = "export")
    public void exportProjectData(HttpServletResponse response) {
        log.info("导出项目资料信息");
        try {
            projectDataService.exportProjectData(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "项目资料信息导入", httpMethod = "POST")
    @PostMapping(value = "import")
    public ResponseEntity<Response> importProjectData(MultipartFile file,Integer importType) {
        log.info("项目资料信息导入");
        List<ProjectData> projectList;
        try {
            projectList = projectDataService.importProjectData(file, importType);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "导入项目资料信息成功！", projectList));
    }

}

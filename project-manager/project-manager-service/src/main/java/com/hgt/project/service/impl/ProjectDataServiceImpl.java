package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectData;
import com.hgt.project.dao.mapper.ProjectDataMapper;
import com.hgt.project.dao.util.ExcelUtil;
import com.hgt.project.service.IProjectDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 项目文件 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
@Slf4j
public class ProjectDataServiceImpl extends ServiceImpl<ProjectDataMapper, ProjectData> implements IProjectDataService {

    @Value("${file-path}")
    private String path;

    @Autowired
    private ProjectDataMapper projectDataMapper;

    @Transactional
    @Override
    public boolean saveWithFile(ProjectData projectData) {
        log.info("保存项目信息并创建目录:{}", projectData.getAddress());
        // 1. 保存项目资料到磁盘上
        boolean result = false;
        File file = new File(path + projectData.getAddress());
        if (!file.isDirectory() && projectData.getFolder() == 0)
            result = file.mkdirs();
        // 2. 保存项目资料到数据库
        if (result)
            result = save(projectData);
        return result;
    }

    @Transactional
    @Override
    public boolean updateWithFileByProject(Project origin, Project news) {
        log.info("更新项目信息并更新目录:{}", news.getProjectName());
        ProjectData projectData;
        QueryWrapper<ProjectData> queryWrapper = new QueryWrapper<>();
        // 1. 更新硬盘上的项目资料
        boolean result = false;
        queryWrapper.eq("project_number", origin.getProjectNumber());
        // 必须是root
        queryWrapper.eq("parent_id", 0L);
        projectData = getOne(queryWrapper);
        projectData.setAddress(news.getProjectName());
        projectData.setFileName(news.getProjectName());
        // 2. 获取原文件夹
        File file = new File(path + origin.getProjectName());
        if (file.exists())
            result = file.renameTo(new File(path + news.getProjectName()));
        if (result)
            result = updateById(projectData);
        return result;
    }

    @Override
    public boolean updateWithFile(ProjectData news) {
        log.info("更新项目资料文件夹信息:{}", news.getFileName());
        ProjectData origin;
        // 1. 更新硬盘上的项目资料
        boolean result = false;
        origin = getById(news);
        File file = new File(path + origin.getAddress());
        if (file.exists())
            result = file.renameTo(new File(path + news.getAddress()));
        if (result)
            result = updateById(news);
        return result;
    }

    @Transactional
    @Override
    public boolean removeWithFileByProject(Project project) {
        log.info("删除项目资料文件夹:{}", project.getProjectName());
        ProjectData projectData;
        QueryWrapper<ProjectData> queryWrapper = new QueryWrapper<>();
        // 1. 通过项目编号查询项目资料
        boolean reslut = false;
        queryWrapper.eq("project_number", project.getProjectNumber());
        queryWrapper.eq("parent_id", 0L);
        projectData = getOne(queryWrapper);
        // 2. 删除磁盘上的文件夹
        File file = new File(path + projectData.getAddress());
        if (file.exists())
            reslut = delAllFile(file);
        if (reslut)
            reslut = remove(queryWrapper);
        return reslut;
    }

    @Override
    public boolean removeWithFile(ProjectData projectData) {
        log.info("删除项目资料文件夹:{}", projectData.getFileName());
        // 1. 删除磁盘上的文件夹及内容
        boolean result = false;
        File file = new File(path + projectData.getAddress());
        if (file.exists())
            result = delAllFile(file);
        if (result)
            result = removeById(projectData);
        return result;
    }

    @Transactional
    @Override
    public boolean upload(MultipartFile file, ProjectData projectData) throws IOException {
        boolean result;
        // 1. 通过id拼接文件路径
        String localPath = path + getBaseMapper().getLocalPath(projectData.getId());
        // 2. 创建当前项目资料文件类
        File local = new File(localPath);
        file.transferTo(local);
        // 3. 保存项目资料信息
        fillValue(file, projectData);
        result = save(projectData);
        return result;
    }

    public String getLocalPath(Long id) {
        return projectDataMapper.getLocalPath(id);
    }

    @Override
    public void exportProjectData(HttpServletResponse response) throws Exception {
        ExcelUtil.exportProjectData(response, list());
    }

    @Override
    public List<ProjectData> importProjectData(MultipartFile file, Integer importType) {

        return null;
    }

    private static boolean delAllFile(File directory) {
        boolean result = false;
        if (!directory.isDirectory()) {
            result = directory.delete();
        } else {
            File[] files = directory.listFiles();
            // 空文件夹
            if (Objects.requireNonNull(files).length == 0) {
                result = directory.delete();
                return result;
            }
            // 删除子文件夹和子文件
            for (File file: files) {
                if (file.isDirectory()) {
                    delAllFile(file);
                } else {
                    result = file.delete();
                }
            }
            if (!result) {
                return false;
            }
            // 删除文件夹本身
            result = directory.delete();
        }
        return result;
    }

    private void fillValue(MultipartFile file, ProjectData projectData) {
        // 通过上传文件填充项目资料信息
        String fileName = file.getOriginalFilename();
        projectData.setFileName(fileName);
        String[] type = fileName != null ? fileName.split("\\.") : new String[0];
        projectData.setFileType(type[type.length - 1]);
        // 带文件后缀
        projectData.setAddress(fileName);
        projectData.setFolder(1);
    }
}

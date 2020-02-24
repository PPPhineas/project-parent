package com.hgt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 项目文件 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IProjectDataService extends IService<ProjectData> {

    /**
     * 保存项目资到数据库以及磁盘上
     * @param projectData 项目资料
     * @return 保存结果
     */
    boolean saveWithFile(ProjectData projectData);

    /**
     * 通过更新项目信息更新项目资料数据库以及磁盘中
     * @param origin 旧的项目
     * @param news 新的项目
     * @return 更新结果
     */
    boolean updateWithFileByProject(Project origin, Project news);

    /**
     * 更新项目资料数据以及磁盘中
     * @param projectData 项目资料
     * @return 更新结果
     */
    boolean updateWithFile(ProjectData projectData);

    /**
     * 通过删除项目删除项目资料数据库以及磁盘中
     * @param project 项目
     * @return 删除结果
     */
    boolean removeWithFileByProject(Project project);

    /**
     * 删除项目资料数据库以及磁盘中
     * @param projectData 项目资料
     * @return 删除结果
     */
    boolean removeWithFile(ProjectData projectData);

    /**
     * 上传项目资料信息
     * @param file 项目资料文件
     * @param projectData 项目资料信息
     * @return 上传结果
     */
    boolean upload(MultipartFile file, ProjectData projectData) throws IOException;

    /**
     * 通过项目资料id获取项目路径
     * @param id 项目资料id
     * @return 硬盘所在路径
     */
    String getLocalPath(Long id);

    void exportProjectData(HttpServletResponse response) throws Exception;

    List<ProjectData> importProjectData(MultipartFile file, Integer importType);

}

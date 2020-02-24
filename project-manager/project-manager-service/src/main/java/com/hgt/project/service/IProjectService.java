package com.hgt.project.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.Project;

/**
 * <p>
 * 项目 服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
public interface IProjectService extends IService<Project> {

	void icsCount(String field,String pNum);

	String whetherExists(String pNum);

	List<Project> importProjectInfo(MultipartFile file, Integer type) throws Exception;
}

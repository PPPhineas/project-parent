package com.hgt.project.dao.entity.vo;

import com.hgt.project.dao.entity.Project;
import com.hgt.project.dao.entity.ProjectEmployee;
import lombok.Data;

import java.util.Collection;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Data
public class ProjectAndEmployeeVO {

    private Project project;

    private Collection<ProjectEmployee> projectEmployees;
}

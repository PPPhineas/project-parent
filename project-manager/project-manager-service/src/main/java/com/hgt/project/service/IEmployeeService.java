package com.hgt.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgt.project.dao.entity.Employee;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xavier
 * @since 2019-12-17
 */
public interface IEmployeeService extends IService<Employee> {

    List<Employee> getEmployeeByProjectNumber(String projectNumber);
}

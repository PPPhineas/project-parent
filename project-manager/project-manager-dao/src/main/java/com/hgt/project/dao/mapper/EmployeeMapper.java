package com.hgt.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgt.project.dao.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xavier
 * @since 2019-12-17
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select employee.* from employee, project_employee where employee.username = project_employee.employee_username and project_employee.project_number = #{projectNumber}")
    List<Employee> getEmployeeByProjectNumber(String projectNumber);
}

package com.hgt.project.service;

import com.hgt.project.dao.entity.Employee;

/**
 *
 * @author karl xavier
 * @version 0.1
*/
public interface IPasswordService {

    void encryptPassword(Employee employee);

    String unEncryptPassword(Employee employee, String password);

}

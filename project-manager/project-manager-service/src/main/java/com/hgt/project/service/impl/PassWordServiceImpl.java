package com.hgt.project.service.impl;

import com.hgt.project.dao.entity.Employee;
import com.hgt.project.service.IPasswordService;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;
/**
 *
 * @author karl xavier
 * @version 0.1
*/
@Service
public class PassWordServiceImpl implements IPasswordService {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    // 没得选, 非对称加密没有实现啊!!!
    private final static String algorithm = "md5";

    private final int hashIterations = 2;
    @Override
    public void encryptPassword(Employee employee) {
        employee.setSalt(randomNumberGenerator.nextBytes().toHex());
        // 做两次哈希
        String newPassword = new SimpleHash(
                algorithm,
                employee.getPassword(),
                ByteSource.Util.bytes(employee.getSalt()),
                hashIterations
        ).toHex();
        employee.setPassword(newPassword);
    }

    @Override
    public String unEncryptPassword(Employee employee, String password) {
        return new SimpleHash(
                algorithm,
                password,
                ByteSource.Util.bytes(employee.getSalt()),
                hashIterations
        ).toHex();
    }
}

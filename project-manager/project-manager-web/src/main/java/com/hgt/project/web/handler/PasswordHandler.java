package com.hgt.project.web.handler;

import com.hgt.project.common.util.GetMD5;
import com.hgt.project.dao.entity.Employee;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.Key;
import java.util.Objects;

/**
 * 处理密码加密
 * @author karl xavier
 * @version 0.1
*/
public class PasswordHandler {

    private final static String algorithm = "md5";

    public void encryptPassword(Employee employee) {
        AesCipherService aesCipherService = new AesCipherService();
        aesCipherService.setKeySize(128);
        Key key = aesCipherService.generateNewKey();
        String encryptText = aesCipherService.encrypt(
                Objects.requireNonNull(
                        // 为了保准给8的倍数，不然会报错
                        GetMD5.next(employee.getUsername())).getBytes()
                , key.getEncoded()).toHex();
        employee.setSalt(encryptText);
        // 做两次哈希
        int hashIterations = 2;
        String newPassword = new SimpleHash(
                algorithm,
                employee.getPassword(),
                ByteSource.Util.bytes(employee.getSalt()),
                hashIterations
        ).toHex();
        employee.setPassword(newPassword);
    }
}

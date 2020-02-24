package com.hgt.project.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hgt.project.dao.util.UserUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 自动填充功能
 * @author karl xavier
 * @version 0.1
 * @see "write something..."
*/
@Component
public class MybatisHandler implements MetaObjectHandler {

    /**
     * 重写自动填充插入方法
     * @param metaObject meteObject类，对应实体类
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 逻辑删除的标识符，0代表正常显示，1代表删除
        setFieldValByName("createUser", getUserCurrentUsername(), metaObject);
        setFieldValByName("updateUser", getUserCurrentUsername(), metaObject);
        setFieldValByName("status", 0, metaObject);
        setFieldValByName("deleted", 0, metaObject);
        setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("uploadUser", LocalDateTime.now(), metaObject);
        setFieldValByName("uploadTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 重写自动填充更新方法
     * @param metaObject meteObject类，对应实体类
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("createUser", getUserCurrentUsername(), metaObject);
        setFieldValByName("updateUser", getUserCurrentUsername(), metaObject);
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        setFieldValByName("readTime", LocalDateTime.now(), metaObject);
        setFieldValByName("uploadUser", LocalDateTime.now(), metaObject);
        setFieldValByName("uploadTime", LocalDateTime.now(), metaObject);
    }

    private String getUserCurrentUsername() {
        if (Objects.isNull(UserUtil.getCurrentUsername())) {
            return "admin";
        }
        return UserUtil.getCurrentUsername();
    }
}

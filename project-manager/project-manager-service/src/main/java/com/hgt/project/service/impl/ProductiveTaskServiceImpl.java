package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.ProductiveTask;
import com.hgt.project.dao.mapper.ProductiveTaskMapper;
import com.hgt.project.service.IProductiveTaskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 生产任务单 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ProductiveTaskServiceImpl extends ServiceImpl<ProductiveTaskMapper, ProductiveTask> implements IProductiveTaskService {

}

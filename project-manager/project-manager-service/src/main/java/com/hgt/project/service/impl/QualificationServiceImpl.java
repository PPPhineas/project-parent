package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Qualification;
import com.hgt.project.dao.mapper.QualificationMapper;
import com.hgt.project.service.IQualificationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资质 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class QualificationServiceImpl extends ServiceImpl<QualificationMapper, Qualification> implements IQualificationService {

}

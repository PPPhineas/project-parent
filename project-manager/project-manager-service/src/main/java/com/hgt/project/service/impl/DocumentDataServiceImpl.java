package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.DocumentData;
import com.hgt.project.dao.mapper.DocumentDataMapper;
import com.hgt.project.service.IDocumentDataService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文档资料 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class DocumentDataServiceImpl extends ServiceImpl<DocumentDataMapper, DocumentData> implements IDocumentDataService {

}

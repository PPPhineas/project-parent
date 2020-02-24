package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Bid;
import com.hgt.project.dao.mapper.BidMapper;
import com.hgt.project.service.IBidService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 投标信息 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class BidServiceImpl extends ServiceImpl<BidMapper, Bid> implements IBidService {

}

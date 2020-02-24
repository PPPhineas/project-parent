package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.PaymentCollectionDetails;
import com.hgt.project.dao.mapper.PaymentCollectionDetailsMapper;
import com.hgt.project.service.IPaymentCollectionDetailsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 合同汇款付款明细表 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class PaymentCollectionDetailsServiceImpl extends ServiceImpl<PaymentCollectionDetailsMapper, PaymentCollectionDetails> implements IPaymentCollectionDetailsService {

}

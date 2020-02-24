package com.hgt.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgt.project.dao.entity.Contract;
import com.hgt.project.dao.mapper.ContractMapper;
import com.hgt.project.service.IContractService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 合同 服务实现类
 * </p>
 *
 * @author xavier
 * @since 2019-12-19
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

}

package com.itheima.mm.service.impl;

import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.CompanyDao;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/15
 * @description ：公司业务实现类
 * @version: 1.0
 */
@Service
@Slf4j
public class CompanyServiceImpl extends BaseService implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Override
	public List<Company> findListAll() {
		List<Company> companyList = companyDao.selectListAll();
		return companyList;
	}
}

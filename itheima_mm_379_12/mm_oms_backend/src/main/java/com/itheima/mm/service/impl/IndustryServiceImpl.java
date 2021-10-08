package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.IndustryDao;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.service.IndustryService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/15
 * @description ：企业行业方向业务实现类
 * @version: 1.0
 */
@Service
public class IndustryServiceImpl extends BaseService implements IndustryService {
	@Autowired
	private IndustryDao industryDao;
	@Override
	public List<Industry> findListAll() {
		List<Industry> industryList =  industryDao.selectListAll();
		return industryList;
	}
}

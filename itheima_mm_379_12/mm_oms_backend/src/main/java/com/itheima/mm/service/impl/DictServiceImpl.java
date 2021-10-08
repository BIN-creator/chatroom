package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.DictDao;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.service.DictService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/15
 * @description ：数据字段业务实现类
 * @version: 1.0
 */
@Service
public class DictServiceImpl extends BaseService implements DictService {
	@Autowired
	private DictDao dictDao;
	@Override
	public List<Dict> selectListByType(Integer typeId) {
		List<Dict> list = dictDao.selectListByType(typeId);
		return list;
	}
}

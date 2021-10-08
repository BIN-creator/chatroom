package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.CatalogDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Catalog;
import com.itheima.mm.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/13
 * @description ：学科目录业务实现类
 * @version: 1.0
 */
@Service
@Slf4j
public class CatalogServiceImpl extends BaseService implements CatalogService {
	@Autowired
	private CatalogDao catalogDao;
	@Override
	public void addCatalog(Catalog catalog) {
		log.info("addCataglog:{}",catalog);
		catalogDao.addCatalog(catalog);

	}
	@Override
	public PageResult findListByPage(QueryPageBean queryPageBean) {
		// 获取数据集
		List<Catalog> catalogList = catalogDao.selectListByPage(queryPageBean);
		// 获取总记录数
		Long totalCount = catalogDao.selectTotalCount(queryPageBean);
		return new PageResult(totalCount,catalogList);
	}

	@Transactional
	@Override
	public void deleteCatalog(Integer id) {
		log.info("deleteCatalog:id:{}",id);
		catalogDao.deleteCatalog(id);
	}
}

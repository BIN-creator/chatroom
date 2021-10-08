package com.itheima.mm.service.impl;

import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.TagDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.exception.MmException;
import com.itheima.mm.pojo.Tag;
import com.itheima.mm.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/13
 * @description ：学科标签业务实现类
 * @version: 1.0
 */
@Service
@Slf4j
public class TagServiceImpl extends BaseService implements TagService {

	@Autowired
	private TagDao tagDao;

	@Transactional
	@Override
	public void addTag(Tag tag){
		log.info("addTag:{}",tag);
		tagDao.addTag(tag);
	}
	@Override
	public PageResult findListByPage(QueryPageBean queryPage) {
		// 获取分页数据集
		List<Tag> tagList = tagDao.selectListByPage(queryPage);
		// 获取基于条件的总记录数
		Long totalCount = tagDao.selectTotalCount(queryPage);
		return new PageResult(totalCount,tagList);
	}

	@Transactional
	@Override
	public void deleteTag(Integer id)throws MmException {
		log.info("addTag,id:{}",id);
		try{
			tagDao.deleteTag(id);
		}catch(RuntimeException e){
			log.error("deleteTag",e);
			throw  new MmException(e.getMessage());
		}
	}
}

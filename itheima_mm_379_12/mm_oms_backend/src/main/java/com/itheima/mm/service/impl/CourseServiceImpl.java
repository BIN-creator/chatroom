package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.exception.MmException;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/11
 * @description ：
 * @version: 1.0
 */
@Slf4j
@Service
public class CourseServiceImpl extends BaseService implements CourseService {

	@Autowired
	private CourseDao courseDao;

	@Transactional
	@Override
	public void addCourse(Course course) {
		log.info("addCourse... course:{}",course);
		courseDao.addCourse(course);
	}

	@Override
	public PageResult findListByPage(QueryPageBean queryPageBean) {
		// 获取分页数据集
		Map queryParams = queryPageBean.getQueryParams();
		List<Course> courseList = courseDao.selectListByNameAndStatus((String)queryParams.get("name"),
			(Integer) queryParams.get("status"),queryPageBean.getOffset(),queryPageBean.getPageSize());//courseDao.selectListByPage(queryPageBean);
		log.info("findListByPage courseList:{}",courseList);
		// 获取记录总数
		Long totalCount = courseDao.selectTotalCount(queryPageBean);
		return new PageResult(totalCount,courseList);
	}

	@Transactional
	@Override
	public void updateCourse(Course course) {
		log.info("updateCourse... course:{}",course);
		courseDao.updateCourse(course);
	}

	@Transactional
	@Override
	public void deleteCourse(Integer id) throws MmException {
		log.info("deleteCourse,id:{}",id);
		Long cataLogQty = courseDao.selectCountCatalogById(id);
		Long tagQty = courseDao.selectCountTagById(id);
		if( cataLogQty > 0 || tagQty > 0  ){
			throw new MmException("学科下有数据，不能删除学科数据");
		}
		courseDao.deleteCourse(id);
	}
	@Override
	public List<Course> findListAll() {
		List<Course> courseList = courseDao.selectListAll();
		return courseList;
	}
}

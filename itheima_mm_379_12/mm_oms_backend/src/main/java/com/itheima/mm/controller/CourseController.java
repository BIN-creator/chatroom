package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.exception.MmException;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.CourseService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/11
 * @description ：学科控制器
 * @version: 1.0
 */
@RequestMapping("/course")
@RestController
@Slf4j
public class CourseController extends BaseController {

	@Autowired
	private CourseService courseService;

	/**
	 * 添加学科
	 * 获取表单数据
	 * 初始化表单数据
	 * 调用Service完成业务
	 * 返回JSON到前端
	 */
	@PostMapping("/add")
	public Result addCourse (HttpServletRequest request, @RequestBody Course course) {
		try{
			log.info("addCourse course:{}",course);
			// 设置创建日期
			course.setCreateDate(DateUtils.parseDate2String(new Date()));
			// 获取当前用户的用户信息
			User user = getSessionUser(request,  GlobalConst.SESSION_KEY_USER);
			if (user != null) {
				course.setUserId(user.getId());
			} else {
				// 调试时，默认是管理员
				course.setUserId(1);
			}
			courseService.addCourse(course);
			return new Result(true,"添加学科成功");
		}catch(RuntimeException e){
			log.error("addCourse",e);
			return new Result(false,"添加学科失败,"+e.getMessage());
		}
	}

	/**
	 * 分页获取学科列表
	 */
	@PostMapping("/findListByPage")
	public Result findListByPage (@RequestBody QueryPageBean pageBean){
		if (pageBean == null){
			pageBean = new QueryPageBean();
			pageBean.setCurrentPage(1);
			pageBean.setPageSize(10);
		}
		log.info("questionList pageBean:{}",pageBean);
		PageResult pageResult = courseService.findListByPage(pageBean);
		return new Result(true,"获取学科列表成功",pageResult);
	}

	@PostMapping("/update")
	public Result updateCourse(@RequestBody Course course){
		try{
			log.info("updateCourse course:{}",course);
			courseService.updateCourse(course);
			return new Result(true,"更新学科成功");
		}catch(RuntimeException e){
			log.error("updateCourse",e);
			return new Result(false,e.getMessage());
		}
	}

	@GetMapping("/delete")
	public Result deleteCourse(String courseId){
		try{
			log.info("deleteCourse courseId:{}",courseId);
			courseService.deleteCourse(Integer.parseInt(courseId));
			return new Result(true,"删除学科成功");
		}catch (MmException me){
			return new Result(false,"删除失败："+me.getMessage());
		}catch(RuntimeException e){
			log.error("deleteCourse",e);
			return new Result(false,"删除失败:"+e.getMessage());
		}
	}

	@GetMapping("/findListAll")
	public Result findListForQuestion (HttpServletRequest request,HttpServletResponse response) {
		try{
			List<Course> courseList = courseService.findListAll();
			return new Result(true,"获取列表成功",courseList);
		}catch(RuntimeException e){
			log.error("findListForQuestion",e);
			return new Result(false,"获取列表失败");
		}
	}

}

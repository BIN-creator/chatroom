package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.QuestionItem;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/12
 * @description ：题目控制器
 * @version: 1.0
 */
@RequestMapping("/question")
@RestController
@Slf4j
public class QuestionController extends BaseController {

	@Autowired
	private QuestionService questionService;



	/**
	 * 获取题目列表
	 */
	@PostMapping("/findListByPage")
	public Result questionList (@RequestBody QueryPageBean pageBean)  {
		if (pageBean == null){
			pageBean = new QueryPageBean();
			pageBean.setCurrentPage(1);
			pageBean.setPageSize(10);
		}
		log.debug("questionList pageBean:{}",pageBean);
		return new Result(true,"获取题目列表成功",questionService.findByPage(pageBean));
	}

	/**
	 * 题目更新和保存
	 * @param request
	 * @param question
	 * @return
	 */
	@PostMapping("/addOrUpdate")
	public Result addOrUpdate(HttpServletRequest request,@RequestBody Question question){
			User user =getSessionUser(request,GlobalConst.SESSION_KEY_USER);
			question.setUserId(user!=null?user.getId():1);
			questionService.addOrUpdate(question);
			return  new Result(true,"更新成功");
	}



	/**
	 * 分页获取精选列表
	 */
	@PostMapping("/findClassicListByPage")
	public Result questionClassicList (@RequestBody QueryPageBean pageBean ) {
		if (pageBean == null){
			pageBean = new QueryPageBean();
			pageBean.setCurrentPage(1);
			pageBean.setPageSize(10);
		}
		log.info("questionList pageBean:{}",pageBean);
		return new Result(true,"获取经典题目列表成功",questionService.findClassicByPage(pageBean));
	}

	/**
	 * 更新题目为经典题目
	 */
	@GetMapping("/updateClassic")
	public  Result updateClassic (String questionId) {
		try{
			log.info("updateClassic questionId:{}",questionId);
			questionService.updateIsClassic(Integer.parseInt(questionId), QuestionConst.ClassicStatus.CLASSIC_STATUS_YES.ordinal());
			return new Result(true,"更新成功");
		}catch(RuntimeException e){
			log.error("updateClassic",e);
			return new Result(false,"更新失败");
		}
	}

	/**
	 * 题目预览
	 */
	@GetMapping("/findById")
	public Result findById(String questionId){
		try{
			Question question = questionService.findQuestionById(Integer.parseInt(questionId));
			return new Result(true,"获取成功",question);
		}catch(RuntimeException e){
			log.error("findById",e);
			return new Result(false,"获取失败,"+e.getMessage());
		}
	}

	/**
	 * 题目下架
	 */
	@PostMapping("/question/updateStatus")
	public Result updateStatus (@RequestBody Map<String,Integer> mapData) {

		try{
			Integer questionId = mapData.get("questionId");
			Integer status = mapData.get("status");
			questionService.updateStatus(questionId,status);
			return new Result(true,"操作成功");
		}catch(Exception e){
		    e.printStackTrace();
			return new Result(false,"操作失败");
		}
	}



}

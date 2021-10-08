package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.exception.MmException;
import com.itheima.mm.pojo.Tag;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.TagService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/13
 * @description ：学科标签控制器
 * @version: 1.0
 */
@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController extends BaseController {

	@Autowired
	private TagService tagService;

	/**
	 * 添加学科标签
	 */
	@PostMapping("/add")
	public Result addTag (HttpServletRequest request,@RequestBody Tag tag){
		try{
			// 初始化用户ID，状态和创建时间
			User user = getSessionUser(request, GlobalConst.SESSION_KEY_USER);
			// catalog.setUserId( user.getId());
			if (user != null) {
				tag.setUserId(user.getId());
			} else {
				// 调试时，默认是管理员
				tag.setUserId(1);
			}
			tag.setStatus(QuestionConst.TagStatus.ENABLE.ordinal());
			tag.setCreateDate(DateUtils.parseDate2String(new Date()));
			tagService.addTag(tag);
			return new Result(true,"添加学科标签成功");
		}catch(RuntimeException e){
			log.error("addTag",e);
			return new Result(false,"添加学科标签失败，"+e.getMessage());
		}
	}

	/**
	 * 分页获取学科标签列表
	 * @throws ServletException
	 * @throws IOException
	 */
	@PostMapping("/findListByPage")
	public Result findListByPage (@RequestBody QueryPageBean pageBean) {
		if (pageBean == null){
			pageBean = new QueryPageBean();
			pageBean.setCurrentPage(1);
			pageBean.setPageSize(10);
		}
		// 获取当前用户的学科方向
		//pageBean.getQueryParams().put("courseId",request.getParameter("courseId"));
		log.info("questionList pageBean:{}",pageBean);
		PageResult pageResult = tagService.findListByPage(pageBean);
		return new Result(true,"获取学科标签列表成功",pageResult);
	}

	/**
	 * 删除学科标签
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@GetMapping("/delete")
	public Result deleteTag (HttpServletRequest request,HttpServletResponse response) {
		try{
			String tagId = request.getParameter("tagId");
			log.info("deleteTag id:{}",tagId);
			tagService.deleteTag(Integer.parseInt(tagId));
			return new Result(true,"删除学科标签成功");
		}catch (MmException me){
			return new Result(false,"删除学科标签失败:"+me.getMessage());
		} catch(RuntimeException re){
			log.error("deleteTag",re);
			return new Result(false,"删除学科标签失败,"+re.getMessage());
		}
	}
}

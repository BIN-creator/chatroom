package com.itheima.mm.controller;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Catalog;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.CatalogService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/13
 * @description ：学科目录控制器
 * @version: 1.0
 */
@RestController
@RequestMapping("/catalog")
@Slf4j
public class CatalogController extends BaseController {

	@Autowired
	private CatalogService catalogService;

	/**
	 * 添加学科目录
	 */
	@PostMapping("/add")
	public Result addCatalog (HttpServletRequest request, @RequestBody Catalog catalog) {
			// 初始化用户ID，状态和创建时间
			User user = getSessionUser(request, GlobalConst.SESSION_KEY_USER);
			if(user != null){
				catalog.setUserId(user.getId());
			}else{
				catalog.setUserId(1);
			}
			catalog.setStatus(0);
			catalog.setCreateDate(DateUtils.parseDate2String(new Date()));
			catalogService.addCatalog(catalog);
			return new Result(true,"添加学科目录成功");
	}

	/**
	 * 分页获取学科目录列表
	 */
	@PostMapping("/findListByPage")
	public Result findListByPage (@RequestBody QueryPageBean pageBean) {
		if (pageBean == null){
			pageBean = new QueryPageBean();
			pageBean.setCurrentPage(1);
			pageBean.setPageSize(10);
		}
		log.info("findListByPage questionList pageBean:{}",pageBean);
		PageResult pageResult = catalogService.findListByPage(pageBean);
		return new Result(true,"获取学科目录列表成功",pageResult);
	}

	/**
	 * 删除学科目录
	 */
	@GetMapping("/delete")
	public Result deleteCatalog (String catalogId){
		try{
			log.info("deleteCatalog id:",catalogId);
			catalogService.deleteCatalog(Integer.parseInt(catalogId));
			return new Result(true,"删除学科目录成功");
		}catch(RuntimeException e){
			log.error("deleteCatalog",e);
			return new Result(false,e.getMessage());
		}
	}
}

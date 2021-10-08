package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.service.IndustryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/15
 * @description ：企业行业行业方向控制器
 * @version: 1.0
 */
@RequestMapping("/industry")
@RestController
@Slf4j
public class IndustryController extends BaseController {

	@Autowired
	private IndustryService industryService;

	@GetMapping("/findListAll")
	public Result findListAll (){
		try{
			List<Industry> industryList = industryService.findListAll();
			return new Result(true,"获取成功",industryList);
		}catch(RuntimeException e){
		    log.error("IndustryController findListAll",e);
		    return new Result(false,"获取失败:"+e.getMessage());
		}
	}
}

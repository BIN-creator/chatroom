package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @description ：企业控制器
 * @version: 1.0
 */
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController extends BaseController {

	@Autowired
	private CompanyService companyService;

	/**
	 * 获取所有企业数据
	 */
	@GetMapping("/findListAll")
	public Result findListAll () {
		try{
			log.info("findListAll");
			List<Company> companyList = companyService.findListAll();
			return new Result(true,"获取成功",companyList);
		}catch(Exception e){
		    log.error("findListAll",e);
		    return new Result(false,"获取失败:"+e.getMessage());
		}
	}
}

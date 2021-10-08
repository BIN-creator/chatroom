package com.itheima.mm.controller;

import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.service.DictService;
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
 * @description ：数据字典控制器
 * @version: 1.0
 */
@RequestMapping("/dict")
@RestController
@Slf4j
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;

	@GetMapping("/findListByType")
	public Result findListByType(String typeId)  {
		try{
			log.info("Dict findListByType typeId:{}",typeId);
			List<Dict> dictList = dictService.selectListByType(Integer.parseInt(typeId));
			return new Result(true,"获取成功",dictList);
		}catch(RuntimeException e){
		    log.error("Dict findListByType",e);
		    return new Result(false,"获取失败:"+e.getMessage());
		}
	}
}

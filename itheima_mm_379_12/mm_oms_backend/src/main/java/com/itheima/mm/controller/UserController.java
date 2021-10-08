package com.itheima.mm.controller;


import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.itheima.mm.common.GlobalConst.JEDIS_POOL;
import static com.itheima.mm.common.GlobalConst.SESSION_KEY_USER;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/9
 * @description ：用户控制器
 * @version: 1.0
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {
	@Autowired
    private UserService userService;


    /**
     * 登录接口
     * 根据用户名获取用户对象
     *   判断用户信息是否正确
     *   登录成功，把用户信息存入session对象
     * 返回JSON结果给前端
     */
    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody User loginForm){
        log.info("loginForm:{}",loginForm);
        User user = userService.findByUsername(loginForm.getUsername());
        if(user == null){
            return new Result(false,"用户名不正确");
        }
        if (user.getPassword().equals(loginForm.getPassword())){
            HttpSession session = request.getSession(true);
            // 把用户对象放入session中
            session.setAttribute(SESSION_KEY_USER,user);
            return new Result(true,"登录成功");
        }else{
            return new Result(false,"密码错误");
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public Result logout (HttpServletRequest request,HttpServletResponse response) {
        log.debug("logout....");
        if( request.getSession(false) != null){
            request.getSession(false).invalidate();
        }
        return  new Result(true,"退出成功");

    }
}

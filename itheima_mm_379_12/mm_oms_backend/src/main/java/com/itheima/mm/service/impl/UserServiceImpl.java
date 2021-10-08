package com.itheima.mm.service.impl;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.UserDao;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author ：seanyang
 * @date ：Created in 2019/8/11
 * @description ：用户业务实现类
 * @version: 1.0
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {
	@Autowired
	private UserDao userDao;
	@Override
	public User findByUsername(String username) {
		log.info("UserServiceImpl findByUsername:{}",username);
		return userDao.findByUsername(username);
	}
}

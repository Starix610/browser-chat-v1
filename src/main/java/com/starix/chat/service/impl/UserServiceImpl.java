package com.starix.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starix.chat.mapper.UserMapper;
import com.starix.chat.entity.User;
import com.starix.chat.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author Tobu
 * @since 2019-12-05 19:16:49
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
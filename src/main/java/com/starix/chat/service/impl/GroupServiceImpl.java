package com.starix.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starix.chat.mapper.GroupMapper;
import com.starix.chat.entity.Group;
import com.starix.chat.service.GroupService;
import org.springframework.stereotype.Service;

/**
 * (Group)表服务实现类
 *
 * @author Tobu
 * @since 2019-12-06 11:03:48
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
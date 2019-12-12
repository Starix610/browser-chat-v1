package com.starix.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starix.chat.mapper.GroupMessageMapper;
import com.starix.chat.entity.GroupMessage;
import com.starix.chat.service.GroupMessageService;
import org.springframework.stereotype.Service;

/**
 * (GroupMessage)表服务实现类
 *
 * @author Tobu
 * @since 2019-12-05 19:16:43
 */
@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements GroupMessageService {

}
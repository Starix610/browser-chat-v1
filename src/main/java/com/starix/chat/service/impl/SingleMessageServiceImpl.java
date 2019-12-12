package com.starix.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starix.chat.mapper.SingleMessageMapper;
import com.starix.chat.entity.SingleMessage;
import com.starix.chat.service.SingleMessageService;
import org.springframework.stereotype.Service;

/**
 * (SingleMessage)表服务实现类
 *
 * @author Tobu
 * @since 2019-12-05 19:16:43
 */
@Service
public class SingleMessageServiceImpl extends ServiceImpl<SingleMessageMapper, SingleMessage> implements SingleMessageService {

}
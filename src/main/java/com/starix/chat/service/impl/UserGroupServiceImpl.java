package com.starix.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starix.chat.mapper.UserGroupMapper;
import com.starix.chat.entity.UserGroup;
import com.starix.chat.service.UserGroupService;
import com.starix.chat.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (UserGroup)表服务实现类
 *
 * @author Tobu
 * @since 2019-12-06 11:03:48
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements UserGroupService {

    @Autowired
    private UserGroupMapper userGroupMapper;


    @Override
    public List<String> getUidsFromGroup(String groupId) {
        return userGroupMapper.selectUidsFromGroup(groupId);
    }

    @Override
    public List<GroupVO> getGroupList() {
        return userGroupMapper.selectGroupListWithCount();
    }
}
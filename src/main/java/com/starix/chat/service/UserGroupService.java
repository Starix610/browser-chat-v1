package com.starix.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starix.chat.entity.UserGroup;
import com.starix.chat.vo.GroupVO;

import java.util.List;

/**
 * (UserGroup)表服务接口
 *
 * @author Tobu
 * @since 2019-12-06 11:03:48
 */
public interface UserGroupService extends IService<UserGroup> {

    List<String> getUidsFromGroup(String groupId);

    List<GroupVO> getGroupList();

}
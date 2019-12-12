package com.starix.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starix.chat.entity.Group;
import com.starix.chat.entity.UserGroup;
import com.starix.chat.vo.GroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserGroup)表数据库访问层
 *
 * @author Tobu
 * @since 2019-12-06 11:03:48
 */
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    List<String> selectUidsFromGroup(@Param("groupId") String groupId);

    List<GroupVO> selectGroupListWithCount();

}
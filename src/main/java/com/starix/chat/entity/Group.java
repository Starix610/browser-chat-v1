package com.starix.chat.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (Group)表实体类
 *
 * @author Tobu
 * @since 2019-12-06 11:03:48
 */
@SuppressWarnings("serial")
@Data
@TableName
@EqualsAndHashCode(callSuper=true)
public class Group extends Model<Group> {
    
    private String id;
    
    private String groupName;
    
    private Date createTime;

}
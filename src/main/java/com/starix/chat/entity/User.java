package com.starix.chat.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (User)表实体类
 *
 * @author Tobu
 * @since 2019-12-05 19:16:43
 */
@SuppressWarnings("serial")
@Data
@TableName
@EqualsAndHashCode(callSuper=true)
public class User extends Model<User> {

    @TableId
    private String uid;
    
    private String username;
    
    private String password;
    
    private String headimgUrl;
    
    private Date createTime;

}
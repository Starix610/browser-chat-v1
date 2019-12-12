package com.starix.chat.entity;

import java.util.Date;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (GroupMessage)表实体类
 *
 * @author Tobu
 * @since 2019-12-05 19:16:43
 */
@SuppressWarnings("serial")
@Data
@TableName
@EqualsAndHashCode(callSuper=true)
public class GroupMessage extends Model<GroupMessage> {
    
    private String id;
    
    private String groupId;
    
    private String fromUid;
    
    private String content;
    
    private Date time;
    //消息状态：0->未接收，1->已接收
    private Integer status;

}
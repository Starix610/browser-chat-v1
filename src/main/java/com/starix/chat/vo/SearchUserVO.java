package com.starix.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tobu
 * @date 2019-12-11 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserVO {

    private String uid;

    private String username;

    private String headimgUrl;

    private Date loginTime;

    //在线状态：0->离线，1->在线
    private Integer status;
}

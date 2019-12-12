package com.starix.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Tobu
 * @date 2019-12-06 11:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String uid;

    private String username;

    private String headimgUrl;

    private Date loginTime;

}

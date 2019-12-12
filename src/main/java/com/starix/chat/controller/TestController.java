package com.starix.chat.controller;

import com.starix.chat.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tobu
 * @date 2019-12-06 12:43
 */
@RestController
public class TestController {

    @Autowired
    private UserGroupService userGroupService;

    @GetMapping("/test")
    public String test(){
        List<String> toUids = userGroupService.getUidsFromGroup("2");
        toUids.removeIf(s->s.equals("11"));
        System.out.println(toUids);
        return "success";
    }

}

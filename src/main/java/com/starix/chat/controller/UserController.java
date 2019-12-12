package com.starix.chat.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.starix.chat.entity.User;
import com.starix.chat.response.CommonResult;
import com.starix.chat.server.WebSocketServer;
import com.starix.chat.service.UserGroupService;
import com.starix.chat.service.UserService;
import com.starix.chat.vo.GroupVO;
import com.starix.chat.vo.SearchUserVO;
import com.starix.chat.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Tobu
 * @date 2019-12-07 10:46
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;



    @GetMapping("/checkUsername")
    public CommonResult checkUsernameExist(String username){

        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        if (user != null){
            return CommonResult.failed("用户名已存在");
        }else {
            return CommonResult.success();
        }
    }

    @PostMapping("/register")
    public CommonResult register(String username, String password){

        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        if (user != null){
            return CommonResult.failed("用户名已存在");
        } else {
            user = new User();
            user.setUid(IdWorker.getIdStr());
            user.setUsername(username);
            user.setPassword(password);
            user.setCreateTime(new Date());
            userService.save(user);
            return CommonResult.success();
        }
    }


    @PostMapping("/login")
    public CommonResult login(String username, String password,HttpSession session){

        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return CommonResult.validateFailed("参数缺失");
        }

        User user = userService.lambdaQuery().eq(User::getUsername, username).one();
        if (user == null || !password.equals(user.getPassword())){
            return CommonResult.failed("用户名或密码错误");
        } else {
            session.setAttribute("user", user);
            return CommonResult.success();
        }
    }


    @GetMapping("/userInfo")
    public CommonResult getUserInfo(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null){
            return CommonResult.failed("用户信息不存在");
        }

        return CommonResult.success(user);
    }

    @GetMapping("/logout")
    public CommonResult userLogout(HttpSession session){
        session.removeAttribute("user");
        return CommonResult.success();
    }


    /**
     * 获得所有在线用户信息
     * @return
     */
    @GetMapping("/userList")
    public CommonResult getUserList(String currentUid){

        List<String> idList = new ArrayList<>();
        for (Map.Entry<String, WebSocketServer> entry : WebSocketServer.webSocketMap.entrySet()){
            if(!entry.getKey().equals(currentUid)){
                idList.add(entry.getKey());
            }
        }
        if (idList.isEmpty()){
            return CommonResult.successMsg("没有在线用户");
        }
        List<User> userList = (List<User>) userService.listByIds(idList);
        List<UserVO> userVOList = new ArrayList<>();
        UserVO userVO;
        for (int i = 0; i < userList.size(); i++) {
            userVO = new UserVO();
            BeanUtils.copyProperties(userList.get(i), userVO);
            userVO.setLoginTime(WebSocketServer.webSocketMap.get(userList.get(i).getUid()).loginTime);
            userVOList.add(userVO);
        }
        return CommonResult.success(userVOList);
    }


    @GetMapping("/groupList")
    public CommonResult getGroupList(){
        List<GroupVO> groupList = userGroupService.getGroupList();
        return CommonResult.success(groupList);
    }

    @GetMapping("/searchUser")
    public CommonResult searchUser(String keyword){
        List<User> userList = userService.lambdaQuery()
                .like(User::getUsername, keyword)
                .or()
                .like(User::getUid, keyword)
                .list();

        if (userList == null || userList.size() == 0){
            return CommonResult.successMsg("没有结果");
        }

        List<SearchUserVO> searchUserVOList = new ArrayList<>();
        SearchUserVO searchUserVO;
        for (int i = 0; i < userList.size(); i++) {
            searchUserVO = new SearchUserVO();
            BeanUtils.copyProperties(userList.get(i), searchUserVO);
            if (WebSocketServer.webSocketMap.containsKey(userList.get(i).getUid())){
                searchUserVO.setLoginTime(WebSocketServer.webSocketMap.get(userList.get(i).getUid()).loginTime);
                searchUserVO.setStatus(1);
            }else {
                searchUserVO.setStatus(0);
            }
            searchUserVOList.add(searchUserVO);
        }

        return CommonResult.success(searchUserVOList);
    }

}

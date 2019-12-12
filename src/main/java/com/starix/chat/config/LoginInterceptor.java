package com.starix.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starix.chat.entity.User;
import com.starix.chat.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Tobu
 * @date 2019-12-07 16:26
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${serverUrl}")
    private String SERVER_URL;


    //在请求处理之前调用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null){
            log.info("用户未登录{}",request.getRequestURL().toString());
            // response.setCharacterEncoding("UTF-8");
            // response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // ObjectMapper mapper = new ObjectMapper();
            // String json = mapper.writeValueAsString(CommonResult.unauthorized(null));
            // response.getWriter().write(json);
            response.sendRedirect(SERVER_URL+"/login.html");
            return false;
        }
        return true;
    }
}

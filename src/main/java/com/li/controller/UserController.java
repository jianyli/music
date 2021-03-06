package com.li.controller;

import com.li.entity.User;
import com.li.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 登录操作
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "/pages/login";
    }
    @RequestMapping("/login")
    public String login(@Param("username") String username, @Param("password") String password, Model model, HttpServletResponse response) throws UnsupportedEncodingException {
        Map<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        String nickname = userService.search(map);
        if (nickname == null) {
            model.addAttribute("errorMsg","用户名或密码错误");
            return "/pages/login";
        }
        Cookie cookie = new Cookie("nickname", URLEncoder.encode(nickname,"UTF-8"));
        cookie.setMaxAge(1 * 60);
        cookie.setPath("/");
        model.addAttribute("nickname",nickname);
        response.addCookie(cookie);
        return "../index";
    }
    /**
     * 注册
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "/pages/register";
    }
    @RequestMapping("/register")
    public String register(User user,Model model) {

        int id = userService.createUser(user);
        if (id == 0) {
            model.addAttribute("errorMsg","注册失败");
            return "/pages/register";
        }
        model.addAttribute("msg","注册成功，请登录");
        return "/pages/login";
    }
}

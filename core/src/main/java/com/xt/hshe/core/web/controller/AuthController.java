package com.xt.hshe.core.web.controller;


import com.xt.hshe.core.pojo.HttpMsg;
import com.xt.hshe.core.util.AES;
import com.xt.hshe.core.util.Consts;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {



    @PostMapping("/login")
    public HttpMsg<String> login(HttpServletRequest request) throws Exception {
        String role = request.getParameter("role");
        Assert.hasText(role, "请选择身份!");
        String id = request.getParameter("id");
        Assert.hasText(id, "用户名不能为空!");
        String password = request.getParameter("password");
        Assert.hasText(password, "密码不能为空!");
        int result = authService.login(id, password);
        if (result== Consts.Auth.NOT_EXIST)
            return new HttpMsg<>(result,"用户不存在~");
        else if (result== Consts.Auth.WRONG_PASSWORD)
            return new HttpMsg<>(result,"密码输入错误~");
        else {
            String sSrc = role + "#" + id + "#" + System.currentTimeMillis();
            String tokenGenerated = AES.Encrypt(sSrc, redisTemplate.opsForValue().get("sKey"));
            return new HttpMsg<>(result,"登陆成功~", tokenGenerated);
        }



    }

}

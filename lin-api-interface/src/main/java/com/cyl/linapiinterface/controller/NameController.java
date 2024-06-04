package com.cyl.linapiinterface.controller;

import javax.servlet.http.HttpServletRequest;

import com.cyl.common.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name,HttpServletRequest request){
        return name;
    }

    @PostMapping("/json")
    public String getNameByJson(@RequestBody User user, HttpServletRequest request) {
        String userName = user.getUserName();
        return "你的名字是" + userName;

    }

}

package com.lym.demo.web;

import com.lym.demo.service.IUserService;
import com.lym.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAllList();
    }

    @GetMapping("/save")
    public void save(@RequestParam String username, @RequestParam Integer gender) {
        userService.insert(username, gender);
    }
}

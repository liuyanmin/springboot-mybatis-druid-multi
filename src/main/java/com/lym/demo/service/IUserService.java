package com.lym.demo.service;

import com.lym.demo.vo.User;

import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
public interface IUserService {

    /**
     * 获取两个库的所有用户
     * @return
     */
    List<User> getAllList();

    /**
     * 同时往两个库中插入数据
     * @param username
     * @param gender
     */
    void insert(String username, Integer gender);
}

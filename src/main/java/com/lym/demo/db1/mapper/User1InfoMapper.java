package com.lym.demo.db1.mapper;

import com.lym.demo.db1.domain.User1Info;

import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
public interface User1InfoMapper {

    List<User1Info> selectList();

    void insert(User1Info user1Info);

    void updateById(User1Info user1Info);

}

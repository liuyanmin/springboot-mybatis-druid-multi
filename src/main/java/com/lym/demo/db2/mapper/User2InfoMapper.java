package com.lym.demo.db2.mapper;

import com.lym.demo.db2.domain.User2Info;

import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
public interface User2InfoMapper {

    List<User2Info> selectList();

    void insert(User2Info user2Info);

    void updateById(User2Info user2Info);

}

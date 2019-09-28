package com.lym.demo.dao;

import com.lym.demo.db2.domain.User2Info;
import com.lym.demo.db2.mapper.User2InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
@Repository
public class User2Dao {

    @Autowired
    private User2InfoMapper user2InfoMapper;

    public List<User2Info> getList() {
        return user2InfoMapper.selectList();
    }

    public void insert(String username, Integer gender) {
        User2Info user2Info = new User2Info();
        user2Info.setUsername(username);
        user2Info.setGender(gender);
        user2Info.setAddTime(LocalDateTime.now());
        user2Info.setUpdateTime(LocalDateTime.now());
        user2InfoMapper.insert(user2Info);
    }

    public void updateById(User2Info user2Info) {
        user2InfoMapper.updateById(user2Info);
    }
}

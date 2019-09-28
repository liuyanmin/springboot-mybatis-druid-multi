package com.lym.demo.dao;

import com.lym.demo.db1.domain.User1Info;
import com.lym.demo.db1.mapper.User1InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
@Repository
public class User1Dao {

    @Autowired
    private User1InfoMapper user1InfoMapper;

    public List<User1Info> getList() {
        return user1InfoMapper.selectList();
    }

    public void insert(String username, Integer gender) {
        User1Info user1Info = new User1Info();
        user1Info.setUsername(username);
        user1Info.setGender(gender);
        user1Info.setAddTime(LocalDateTime.now());
        user1Info.setUpdateTime(LocalDateTime.now());
        user1InfoMapper.insert(user1Info);
    }

    public void updateById(User1Info user1Info) {
        user1InfoMapper.updateById(user1Info);
    }
}

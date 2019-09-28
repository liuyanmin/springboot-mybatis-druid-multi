package com.lym.demo.service.impl;

import com.lym.demo.dao.User1Dao;
import com.lym.demo.dao.User2Dao;
import com.lym.demo.db1.domain.User1Info;
import com.lym.demo.db2.domain.User2Info;
import com.lym.demo.service.IUserService;
import com.lym.demo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyanmin on 2019/9/28.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private User1Dao user1Dao;
    @Autowired
    private User2Dao user2Dao;

    @Override
    public List<User> getAllList() {
        List<User1Info> user1InfoList = user1Dao.getList();
        List<User2Info> user2InfoList = user2Dao.getList();

        List<User> userList = new ArrayList<>();
        if (user1InfoList != null) {
            for (User1Info user1Info : user1InfoList) {
                User user = new User();
                user.setId(user1Info.getId());
                user.setUsername(user1Info.getUsername());
                user.setGender(user1Info.getGender());
                user.setAddTime(user1Info.getAddTime());
                user.setUpdateTime(user1Info.getUpdateTime());
                userList.add(user);
            }
        }

        if (user2InfoList != null) {
            for (User2Info user2Info : user2InfoList) {
                User user = new User();
                user.setId(user2Info.getId());
                user.setUsername(user2Info.getUsername());
                user.setGender(user2Info.getGender());
                user.setAddTime(user2Info.getAddTime());
                user.setUpdateTime(user2Info.getUpdateTime());
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    @Transactional(value = "db2TransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 15, rollbackFor = Exception.class)
    public void insert(String username, Integer gender) {
        user1Dao.insert(username, gender);

        user2Dao.insert(username, gender);

        // 抛出运行时异常
        throw new RuntimeException("抛出异常");

        // 最终结果，user1插入成功，user2插入失败
    }
}

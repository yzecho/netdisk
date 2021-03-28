package io.yzecho.netdisk.service.impl;

import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.service.BaseService;
import io.yzecho.netdisk.service.UserService;
import io.yzecho.netdisk.model.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 20:08
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Override
    public boolean insert(User user) {
        return userMapper.insert(user) == 1;
    }

    @Override
    public boolean deleteUserByUserId(Integer userId) {
        return userMapper.deleteUserByUserId(userId) == 1;
    }

    @Override
    public User queryUserByUserId(Integer userId) {
        return userMapper.queryUserByUserId(userId);
    }

    @Override
    public User queryUserByGithubId(String githubId) {
        return userMapper.queryUserByGithubId(githubId);
    }

    @Override
    public User queryUserByEmail(String email) {
        return userMapper.queryUserByEmail(email);
    }

    @Override
    public List<User> queryAllByLimit() {
        return userMapper.queryAll();
    }

    @Override
    public List<User> queryAll(User user) {
        return userMapper.queryAll(user);
    }

    @Override
    public boolean update(User user) {
        return userMapper.update(user);
    }

    @Override
    public List<UserVO> queryUsers() {
        return userMapper.queryUsers();
    }

    @Override
    public Integer getUsersCount() {
        return userMapper.getUsersCount();
    }
}

package io.yzecho.netdisk.service;

import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.model.vo.*;


import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 13/01/2021 21:34
 */
public interface UserService {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    boolean insert(User user);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    boolean deleteUserByUserId(Integer userId);

    /**
     * 查询单条数据
     *
     * @param userId
     * @return
     */
    User queryUserByUserId(Integer userId);

    /**
     * 通过githubId查询单条数据
     *
     * @param githubId
     * @return
     */
    User queryUserByGithubId(String githubId);

    /**
     * 通过邮箱查询单条数据
     *
     * @param email
     * @return
     */
    User queryUserByEmail(String email);

    /**
     * 查询全部用户
     *
     * @return
     */
    List<User> queryAllByLimit();

    /**
     * 实体作为筛选条件
     *
     * @return
     */
    List<User> queryAll(User user);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    boolean update(User user);

    /**
     * 获取全部UserVO
     *
     * @return
     */
    List<UserVO> queryUserVOs(Integer page, Integer size);

    /**
     * 获取已注册的全部用户数
     *
     * @return
     */
    Integer getUsersCount();

    /**
     * 获取用户的全部文件夹数
     *
     * @return
     */
    Integer getUserFolderCount(Integer userId);
}

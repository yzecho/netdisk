package io.yzecho.netdisk.mapper;

import io.yzecho.netdisk.model.User;
import io.yzecho.netdisk.model.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yzecho
 * @desc
 * @date 16/01/2021 12:07
 */
@Mapper
public interface UserMapper {

    /**
     * 添加用户
     *
     * @param user
     * @return 受影响的行数
     */
    int insert(User user);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    int deleteUserByUserId(Integer userId);

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
    List<User> queryAll();

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
    List<UserVO> queryUsers();

    /**
     * 获取已注册的全部用户数
     *
     * @return
     */
    Integer getUsersCount();
}

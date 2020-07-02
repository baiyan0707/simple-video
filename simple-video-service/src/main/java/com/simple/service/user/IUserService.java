package com.simple.service.user;

import com.simple.dao.model.user.User;

import java.util.Optional;

/**
 * @author bai
 */
public interface IUserService {

    /**
     * 根据openid获取用户信息
     * @param openid
     * @return
     */
    Optional<User> findUserByOpenId(String openid);

    /**
     * 添加用户
     * @param user
     */
    void insertUserInfo(User user);
}

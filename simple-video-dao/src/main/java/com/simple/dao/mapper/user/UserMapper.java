package com.simple.dao.mapper.user;

import com.simple.dao.model.user.User;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * @author bai
 */
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据openid获取用户信息
     * @param openid
     * @return
     */
    Optional<User> findUserByOpenId(@Param("openid") String openid);
}
package com.simple.service.impl.user;

import com.simple.commons.exception.GlobalException;
import com.simple.commons.exception.SimpleVideoErrorCode;
import com.simple.dao.mapper.user.UserMapper;
import com.simple.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bai
 * @Description 用户操作
 * @Date 2020/3/11 10:52 PM
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @see IUserService#insertUserInfo(User)
     * @param user
     * @throws GlobalException
     */
    @Override
    public void insertUserInfo(User user) throws GlobalException {
        int result = userMapper.insertSelective(user);

        if(result == 0){
            log.error("用户添加异常");
            throw new GlobalException(SimpleVideoErrorCode.SIMPLEVIDEO_ERROR_CODE_AA0001);
        }
    }

    /**
     * @see IUserService#findUserByOpenId(String)
     * @param openid
     * @return
     */
    @Override
    public User findUserByOpenId(String openid) {
        return userMapper.findUserByOpenId(openid);
    }
}

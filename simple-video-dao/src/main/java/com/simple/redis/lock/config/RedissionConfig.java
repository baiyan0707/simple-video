package com.simple.redis.lock.config;

import com.simple.redis.lock.impl.RedisLockImpl;
import com.simple.redis.utils.RedisLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 本类用作redis的配置
 */
@Service
public class RedissionConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress(host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public RedisLockImpl redissonLocker(RedissonClient redissonClient){
        RedisLockImpl locker = new RedisLockImpl(redissonClient);
        //设置LockUtil的锁处理对象
        RedisLockUtil.setLocker(locker);
        return locker;
    }
}

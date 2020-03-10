package com.simple.web.http;

import com.simple.api.ISimpleVideoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bai
 * @Description
 * @Date 2020/3/10 12:33 PM
 */
@RestController
@Slf4j
public class SimpleVideoApiImpl implements ISimpleVideoApi {

    @PostMapping("/apis/test")
    @Override
    public String test(String str) {
        return str;
    }
}

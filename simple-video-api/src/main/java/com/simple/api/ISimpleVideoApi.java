package com.simple.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author by bai on 2020/3/10 12:30 PM
 */
@Api(value = "简视频接口",tags = "simple-video-api")
public interface ISimpleVideoApi {

    @ApiOperation("测试接口")
    String test(String str);
}

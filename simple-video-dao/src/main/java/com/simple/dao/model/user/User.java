package com.simple.dao.model.user;

/**
 * @author bai
 * @Description
 * @Date 2020/7/2 10:23 AM
 * @github https://github.com/baiyan0707
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 3567308146320965805L;

    private Long id;

    private String openId;

    private Integer phone;

    private String nickname;

    private Integer sex;

    private String avatar;

    private Integer attentions;

    private Integer fans;

    private String mask;

    private Date createAt;

    private Date updateAt;

    private String createBy;

    private String updateBy;
}

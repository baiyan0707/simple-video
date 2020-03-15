package com.simple.dao.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author bai
 * @Description
 * @Date 2020/3/11 10:20 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 3567308146320965805L;

    private Long id;

    private String openId;

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

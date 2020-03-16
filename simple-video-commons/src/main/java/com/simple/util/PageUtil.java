package com.simple.util;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huguangpu
 * @date 2019-07-05 14:24
 * @description
 */
@Data
@ToString
public class PageUtil<T> {

    /**
     * 实体类列表
     */
    List<T> content;
    /**
     * 是否首页
     */
    boolean first;
    /**
     * 是否尾页
     */
    boolean last;
    /**
     * 总记录数
     */
    Integer totalElements;
    /**
     * 总页数
     */
    Integer totalPages;

    Integer numberOfElements;
    /**
     * 每页记录数
     */
    Integer size;
    /**
     * 当前页
     */
    Integer number;


    public void pageUtil(Integer page, Integer size, List<T> list) {
        List<T> list1 = list.stream().skip(page * size).limit(size).collect(Collectors.toList());
        int length = list.size();
        //是否第一页
        this.first = (page == 0);
        //是否最后一页
        this.last = (page == (length - 1) / size);
        //总页数
        this.totalPages = ((length - 1) / size + 1);
        //总elements
        this.totalElements = (length);
        //每页多少elements
        this.size = (size);
        //内容
        this.content = (list1);
        //当前页elements
        this.numberOfElements = (list1.size());
        //当前页数，第一页是0
        this.number = (page);
    }


}

package com.simple.util;

/**
 * @ClassName PageUtils
 * @Author Bai
 * @Date 2019/12/20 11:51
 * @Version 1.0
 * @description
 **/
public class PageUtils {

    // 当前页（从页面获取的当前页码，未计算）
    private int currentPage;
    // 前一页
    private int prevPage;
    // 下一页
    private int nextPage;
    // 尾页
    private int lastPage;
    // 总记录数
    private int count;
    // 每页的条数
    private int pageSize = 3;
    // 分页计入数（使用时调用的初始页，计算后）
    private int pageRecord;
    // 页面分页模型（传入页面使用的DOM）
    private String page;
    // 有参构造器
    public PageUtils(String currentPage, int count, int pageSize) {
        init(currentPage, count, pageSize);
        initLastPage();
        initCurrentPage();
        initPrevPage();
        initNextPage();
        initPageRecord();
        initPage();
    }
    // 初始化三个重要元素
    private void init(String currentPage, int count, int pageSize) {
        if (currentPage == null || "".equals(currentPage) || "0".equals(currentPage)) {
            currentPage = "1";
        }
        this.currentPage = Integer.parseInt(currentPage);
        this.count = count;
        this.pageSize = pageSize;
    }
    // 初始化尾页
    private void initLastPage() {
        if (count % pageSize == 0) {
            lastPage = count / pageSize;
        } else {
            lastPage = count / pageSize + 1;
        }
    }
    // 初始化并矫正当前页(防止外部访问出错)
    private void initCurrentPage() {
        if (currentPage < 1) {
            currentPage = 1;
        } else if (currentPage > lastPage) {
            currentPage = lastPage;
        }
    }
    // 初始化上一页
    private void initPrevPage() {
        if (currentPage != 1) {
            prevPage = currentPage - 1;
        }else{
            prevPage = 1;
        }
    }
    // 初始化下一页
    private void initNextPage() {
        if (currentPage != lastPage) {
            nextPage = currentPage + 1;
        }else{
            nextPage = lastPage;
        }
    }
    // 初始化分页计入数
    private void initPageRecord() {
        pageRecord = (currentPage - 1) * pageSize;
        if(pageRecord < 0){
            pageRecord = 0;
        }
    }

    /**
     * 初始化页面分页模型（按键中的class属性是bootstrap的样式）
     */
    private void initPage() {
        page = "第" + currentPage + "/" + lastPage + "页，共" + count + "条记录。";
        page += "<input type='button' value='首页' οnclick='page(1)' class='btn btn-sm'/>";
        page += "<input type='button' value='上一页' οnclick='page(" + prevPage + ")' class='btn btn-sm'/>";
        page += "<input type='button' value='下一页' οnclick='page(" + nextPage + ")' class='btn btn-sm'/>";
        page += "<input type='button' value='尾页' οnclick='page(" + lastPage + ")' class='btn btn-sm'/>";
    }

    /**
     * 对外访问通道
     * @return
     */
    public int getCurrentPage() {
        return currentPage;
    }
    public int getPrevPage() {
        return prevPage;
    }
    public int getNextPage() {
        return nextPage;
    }
    public int getLastPage() {
        return lastPage;
    }
    public int getCount() {
        return count;
    }
    public int getPageSize() {
        return pageSize;
    }
    public int getPageRecord() {
        return pageRecord;
    }
    public String getPage() {
        return page;
    }
}

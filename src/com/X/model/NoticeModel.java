package com.X.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class NoticeModel {

    List<NewsModel> list;
    PageModel page;

    public List<NewsModel> getList() {
        return list;
    }

    public void setList(List<NewsModel> list) {
        this.list = list;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }
}

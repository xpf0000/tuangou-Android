package com.X.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class SearchKeyModel implements Serializable {

    private List<String> searchKeys = new ArrayList<>();

    public List<String> getSearchKeys() {
        return searchKeys;
    }

    public void setSearchKeys(List<String> searchKeys) {
        this.searchKeys = searchKeys;
    }
}

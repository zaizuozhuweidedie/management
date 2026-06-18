package com.itheima.pojo;

import lombok.Data;

@Data
public class LogQueryParam {
    private Integer page = 1;
    private Integer pageSize = 10;

    public int safePage() {
        return (page == null || page < 1) ? 1 : page;
    }

    public int safePageSize() {
        return (pageSize == null || pageSize < 1) ? 10 : pageSize;
    }
}


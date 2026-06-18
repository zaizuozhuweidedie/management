package com.itheima.pojo;

import lombok.Data;

@Data
public class StudentQueryParam {
    private String name;
    private Integer degree;
    private Integer clazzId;
    private Integer page;
    private Integer pageSize;

    public int safePage() {
        return (page == null || page < 1) ? 1 : page;
    }

    public int safePageSize() {
        return (pageSize == null || pageSize < 1) ? 10 : pageSize;
    }
}


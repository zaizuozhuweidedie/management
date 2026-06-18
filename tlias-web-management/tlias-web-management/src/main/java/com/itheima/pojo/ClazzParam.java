package com.itheima.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ClazzParam {
    private String name; //班级名称
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;
    private Integer page = 1; //页码
    private Integer pageSize = 10; //每页展示记录数

    public int safePage() {
        return (page == null || page < 1) ? 1 : page;
    }

    public int safePageSize() {
        return (pageSize == null || pageSize < 1) ? 10 : pageSize;
    }
}

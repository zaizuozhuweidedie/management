package com.itheima.service;

import com.itheima.pojo.LogQueryParam;
import com.itheima.pojo.PageResult;

public interface LogService {
    PageResult page(LogQueryParam logQueryParam);
}


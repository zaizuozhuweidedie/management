package com.itheima.service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

public interface StudentService {
    PageResult page(StudentQueryParam studentQueryParam);

    Student getById(Integer id);

    void save(Student student);

    void handleViolation(Integer id, Integer score);

    void deleteByIds(String ids);
}

package com.itheima.service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzParam;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface ClazzService {
    /**
     * 班级条件分页查询
     */
    PageResult page(ClazzParam clazzParam);

    /**
     * 根据ID删除班级
     */
    void deleteById(Integer id);

    /**
     * 新增班级
     */
    void save(Clazz clazz);

    /**
     * 修改班级
     */
    void update(Clazz clazz);

    /**
     * 根据ID查询班级
     */
    Clazz getById(Integer id);

    /**
     * 查询所有班级
     */
    List<Clazz> listAll();
}
// 本轮不声明学员管理、统计相关 service 方法

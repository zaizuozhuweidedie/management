package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.exception.BusinessException;
import com.itheima.mapper.ClazzMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult page(ClazzParam clazzParam) {
        PageHelper.startPage(clazzParam.safePage(), clazzParam.safePageSize());

        List<Clazz> clazzList = clazzMapper.list(clazzParam);
        fillStatus(clazzList);

        Page<Clazz> page = (Page<Clazz>) clazzList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Integer id) {
        Integer count = studentMapper.countByClazzId(id);
        if (count != null && count > 0) {
            throw new BusinessException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public void save(Clazz clazz) {
        LocalDateTime now = LocalDateTime.now();
        clazz.setCreateTime(now);
        clazz.setUpdateTime(now);
        clazzMapper.insert(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> listAll() {
        List<Clazz> clazzList = clazzMapper.listAll();
        fillStatus(clazzList);
        return clazzList;
    }

    private void fillStatus(List<Clazz> clazzList) {
        if (clazzList == null || clazzList.isEmpty()) {
            return;
        }
        LocalDate now = LocalDate.now();
        for (Clazz clazz : clazzList) {
            if (clazz.getBeginDate() == null || clazz.getEndDate() == null) {
                continue;
            }
            if (now.isBefore(clazz.getBeginDate())) {
                clazz.setStatus("未开班");
            } else if (now.isAfter(clazz.getEndDate())) {
                clazz.setStatus("已结课");
            } else {
                clazz.setStatus("已开班");
            }
        }
    }
}

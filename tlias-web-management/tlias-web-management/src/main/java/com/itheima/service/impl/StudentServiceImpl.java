package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.exception.BusinessException;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.safePage(), studentQueryParam.safePageSize());
        List<Student> studentList = studentMapper.pageList(studentQueryParam);
        Page<Student> page = (Page<Student>) studentList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public Student getById(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException("学员ID不合法");
        }
        Student student = studentMapper.getById(id);
        if (student == null) {
            throw new BusinessException("学员不存在");
        }
        return student;
    }

    @Override
    public void save(Student student) {
        if (student == null) {
            throw new BusinessException("请求参数不能为空");
        }
        if (!StringUtils.hasText(student.getName())
                || !StringUtils.hasText(student.getNo())
                || student.getGender() == null
                || !StringUtils.hasText(student.getPhone())
                || student.getDegree() == null
                || student.getClazzId() == null) {
            throw new BusinessException("缺少必填参数");
        }

        if (student.getGender() != 1 && student.getGender() != 2) {
            throw new BusinessException("性别参数不合法");
        }
        if (student.getDegree() < 1 || student.getDegree() > 6) {
            throw new BusinessException("学历参数不合法");
        }

        if (student.getViolationCount() == null) {
            student.setViolationCount((short) 0);
        }
        if (student.getViolationScore() == null) {
            student.setViolationScore((short) 0);
        }

        LocalDateTime now = LocalDateTime.now();
        student.setCreateTime(now);
        student.setUpdateTime(now);
        studentMapper.insert(student);
    }

    @Override
    public void handleViolation(Integer id, Integer score) {
        if (id == null || id <= 0) {
            throw new BusinessException("学员ID不合法");
        }
        if (score == null || score <= 0) {
            throw new BusinessException("违纪扣分必须大于0");
        }
        int rows = studentMapper.increaseViolation(id, score);
        if (rows == 0) {
            throw new BusinessException("学员不存在");
        }
    }

    @Override
    public void deleteByIds(String ids) {
        if (ids == null || ids.trim().isEmpty()) {
            throw new BusinessException("学员ID不能为空");
        }

        String[] idArray = ids.split(",");
        boolean hasBlankId = Arrays.stream(idArray).anyMatch(id -> id == null || id.trim().isEmpty());
        if (hasBlankId) {
            throw new BusinessException("学员ID格式不正确");
        }

        try {
            List<Integer> idList = Arrays.stream(idArray)
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .distinct()
                    .toList();
            studentMapper.deleteByIds(idList);
        } catch (NumberFormatException e) {
            throw new BusinessException("学员ID格式不正确");
        }
    }
}

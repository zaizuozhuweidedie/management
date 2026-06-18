package com.itheima.service;

import com.itheima.pojo.JobOption;
import com.itheima.pojo.StudentCountOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    /**
     * 统计各个职位的员工人数
     * @return
     */
    JobOption getEmpJobData();

    List<Map<String, Object>> getEmpGenderData();

    List<Map<String, Object>> getStudentDegreeData();

    StudentCountOption getStudentCountData();
}

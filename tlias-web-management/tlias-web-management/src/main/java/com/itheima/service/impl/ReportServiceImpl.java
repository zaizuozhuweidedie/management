package com.itheima.service.impl;

import com.itheima.mapper.StudentMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.StudentCountOption;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService{
    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map<String, Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        List<Map<String, Object>> degreeDataList = studentMapper.countStudentDegreeData();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> degreeData : degreeDataList) {
            Integer degree = degreeData.get("degree") == null ? null : ((Number) degreeData.get("degree")).intValue();
            String degreeName = getDegreeName(degree);
            Number total = (Number) degreeData.get("total");

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name", degreeName);
            data.put("value", total == null ? 0 : total.intValue());
            result.add(data);
        }
        return result;
    }

    @Override
    public StudentCountOption getStudentCountData() {
        List<Map<String, Object>> countDataList = studentMapper.countStudentCountData();
        List<String> clazzList = countDataList.stream()
                .map(dataMap -> String.valueOf(dataMap.get("clazzName")))
                .toList();
        List<Integer> dataList = countDataList.stream()
                .map(dataMap -> ((Number) dataMap.get("total")).intValue())
                .toList();
        return new StudentCountOption(clazzList, dataList);
    }

    private String getDegreeName(Integer degree) {
        if (degree == null) {
            return "未知学历";
        }
        return switch (degree) {
            case 1 -> "初中";
            case 2 -> "高中";
            case 3 -> "大专";
            case 4 -> "本科";
            case 5 -> "硕士";
            case 6 -> "博士";
            default -> "未知学历";
        };
    }
}

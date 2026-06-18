package com.itheima.mapper;

import com.itheima.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    @Select("select ol.id, ol.operate_emp_id, ol.operate_time, ol.class_name, ol.method_name, ol.method_params, " +
            "ol.return_value, ol.cost_time, e.name as operate_emp_name " +
            "from operate_log ol left join emp e on ol.operate_emp_id = e.id " +
            "order by ol.operate_time desc, ol.id desc")
    List<OperateLog> page();
}

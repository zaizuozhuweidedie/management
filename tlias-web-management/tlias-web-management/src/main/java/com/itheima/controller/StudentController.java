package com.itheima.controller;


import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("学员列表条件分页查询, 参数: {}", studentQueryParam);
        return Result.success(studentService.page(studentQueryParam));
    }

    @GetMapping("/{id:\\d+}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询学员, id: {}", id);
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学员, 参数: {}", student);
        studentService.save(student);
        return Result.success();
    }

    @PutMapping("/violation/{id:\\d+}/{score:\\d+}")
    public Result handleViolation(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("违纪处理, id: {}, score: {}", id, score);
        studentService.handleViolation(id, score);
        return Result.success();
    }

    @DeleteMapping("/{ids:[\\d,]+}")
    public Result deleteByIds(@PathVariable String ids) {
        log.info("批量删除学员, ids: {}", ids);
        studentService.deleteByIds(ids);
        return Result.success();
    }
}

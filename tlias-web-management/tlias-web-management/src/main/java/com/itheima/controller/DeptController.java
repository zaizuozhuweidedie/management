package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    // 查询所有部门列表
    @GetMapping
    public Result list() {
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    // 根据id删除部门
    @DeleteMapping
    public Result delete(Integer id) {
        System.out.println("删除部门，id=" + id);
        deptService.deleteById(id);
        return Result.success();
    }

    // 添加部门
    @PostMapping
    public Result add(@RequestBody Dept dept) {
        System.out.println("添加部门，dept=" + dept);
        deptService.add(dept);
        return Result.success();
    }

    // 根据id查询部门
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        System.out.println("根据id查询部门，id=" + id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    // 修改部门
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        System.out.println("修改部门，dept=" + dept);
        deptService.update(dept);
        return Result.success();
    }
}

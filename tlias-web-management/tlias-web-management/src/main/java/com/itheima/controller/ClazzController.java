package com.itheima.controller;

import com.itheima.anno.LogOperation;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    // 班级列表条件分页查询: GET /clazzs
    @LogOperation
    @GetMapping
    public Result page(ClazzParam clazzParam) {
        log.info("查询班级列表分页查询, 参数: {}", clazzParam);
        PageResult pageResult = clazzService.page(clazzParam);
        return Result.success(pageResult);
    }

    // 查询所有班级: GET /clazzs/list
    @LogOperation
    @GetMapping("/list")
    public Result list() {
        log.info("查询所有班级");
        return Result.success(clazzService.listAll());
    }

    // 删除班级: DELETE /clazzs/{id}，仅允许数字id
    @LogOperation
    @DeleteMapping("/{id:\\d+}")
    public Result deleteById(@PathVariable Integer id) {
        log.info("删除班级, id: {}", id);
        clazzService.deleteById(id);
        return Result.success();
    }

    // 新增班级: POST /clazzs
    @LogOperation
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级, 参数: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    // 根据ID查询班级: GET /clazzs/{id}
    @LogOperation
    @GetMapping("/{id:\\d+}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询班级, id: {}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    // 修改班级: PUT /clazzs
    @LogOperation
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级, 参数: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }
}

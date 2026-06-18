package com.itheima.aop;

import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private static final int CLASS_NAME_MAX_LEN = 255;
    private static final int METHOD_NAME_MAX_LEN = 100;
    private static final int METHOD_PARAMS_MAX_LEN = 2000;
    private static final int RETURN_VALUE_MAX_LEN = 1000;

    @Autowired
    private OperateLogMapper operateLogMapper;

    // 全量记录 controller 层操作
    @Around("execution(* com.itheima.controller..*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable throwable = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            throwable = ex;
            throw ex;
        } finally {
            OperateLog operateLog = new OperateLog();
            operateLog.setOperateEmpId(getCurrentUserId());
            operateLog.setOperateTime(LocalDateTime.now());
            operateLog.setClassName(truncate(joinPoint.getTarget().getClass().getName(), CLASS_NAME_MAX_LEN));
            operateLog.setMethodName(truncate(joinPoint.getSignature().getName(), METHOD_NAME_MAX_LEN));
            operateLog.setMethodParams(truncate(Arrays.toString(joinPoint.getArgs()), METHOD_PARAMS_MAX_LEN));

            String returnValue = (throwable == null) ? String.valueOf(result) : throwable.getClass().getSimpleName() + ": " + throwable.getMessage();
            operateLog.setReturnValue(truncate(returnValue, RETURN_VALUE_MAX_LEN));
            operateLog.setCostTime(System.currentTimeMillis() - startTime);

            try {
                operateLogMapper.insert(operateLog);
            } catch (Exception e) {
                // 日志落库失败不影响主业务
                log.warn("操作日志写入失败, class={}, method={}", operateLog.getClassName(), operateLog.getMethodName(), e);
            }
        }
    }

    private Integer getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }

    private String truncate(String value, int maxLen) {
        if (value == null || value.length() <= maxLen) {
            return value;
        }
        return value.substring(0, maxLen);
    }
}
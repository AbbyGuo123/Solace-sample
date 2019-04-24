package com.ars.aspect;

import com.ars.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Ocean Liang
 * @date 3/10/2019
 */
@Aspect
@Component
@Slf4j
public class ActivityExceptionAspect {
    @Around("execution(* com.ars.controller.ActivityController.*(..))")
    public Object duringExecuteActivity(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error(String.format("thrown by %s, %s", joinPoint.getSignature().toShortString(), e.getMessage()));
            return ResponseDto.fail("execute activity has exception");
        }
    }
}

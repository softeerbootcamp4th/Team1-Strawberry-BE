package com.hyundai.softeer.backend.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodExecutionTimeAspect {
    private static final Logger logger = LoggerFactory.getLogger(MethodExecutionTimeAspect.class);
    @Around("@annotation(com.hyundai.softeer.backend.global.aop.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        logger.debug("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}

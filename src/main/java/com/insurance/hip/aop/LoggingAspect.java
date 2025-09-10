package com.insurance.hip.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

// LoggingAspect.java
@Aspect
@Component
public class LoggingAspect {

    // Pointcut: all methods in service package
    @Pointcut("execution(* com.insurance.hip.aop.service.*.*(..))")
    public void serviceMethods() {}

    // Before advice
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before: " + joinPoint.getSignature().getName());
    }

    // After returning advice
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        System.out.println("After: " + joinPoint.getSignature().getName() + " Result: " + result);
    }
}


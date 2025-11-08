package com.shopease.product.audit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Before("execution(* com.shopease.product.service.ProductService.saveProduct(..))")
    public void auditProductCreation(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("AUDIT: Product creation attempted at {} with data: {}", 
                LocalDateTime.now(), args[0]);
    }

    @AfterReturning(value = "execution(* com.shopease.product.service.ProductService.saveProduct(..))", 
                   returning = "result")
    public void auditProductCreated(JoinPoint joinPoint, Object result) {
        log.info("AUDIT: Product created successfully at {} - Result: {}", 
                LocalDateTime.now(), result);
    }

    @Before("execution(* com.shopease.product.service.ProductService.deleteProduct(..))")
    public void auditProductDeletion(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("AUDIT: Product deletion attempted at {} for ID: {}", 
                LocalDateTime.now(), args[0]);
    }
}
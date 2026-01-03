package com.library.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        // Log request details
        log.info("=== Request Start ===");
        log.info("Method: {}", request.getMethod());
        log.info("URL: {}", request.getRequestURL());
        log.info("IP: {}", request.getRemoteAddr());
        log.info("Class: {}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("Method: {}", joinPoint.getSignature().getName());
        log.info("Args: {}", Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            // Log response details
            log.info("=== Response ===");
            log.info("Execution Time: {} ms", executionTime);
            log.info("Response: {}", objectMapper.writeValueAsString(result));
            log.info("=== Request End ===");

            return result;
        } catch (Exception e) {
            log.error("=== Exception ===");
            log.error("Exception: {}", e.getMessage(), e);
            log.error("=== Request End ===");
            throw e;
        }
    }
}

package com.mphasis.cqrs.os.command.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(com.mphasis.cqrs.os.command.aspects.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        Object proceed = joinPoint.proceed();

        stopWatch.stop();

        log.info("\"{}\" executed in {} ms ", joinPoint.getSignature(), stopWatch.getTotalTimeMillis());

        return proceed;
    }
}

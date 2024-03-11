package io.elice.shoppingmall.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class LoggingAspect {

    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* io.elice.shoppingmall.*.*.*.*(..))")
    private void orderController() {}

    @Before("orderController()")
    public void beforeLog(JoinPoint joinPoint) {
        logger.info("다음 {} 메서드를 실행합니다!: ", joinPoint.getSignature().getName());
    }

    @After("orderController()")
    public void afterLog(JoinPoint joinPoint) {
        logger.info("다음 메서드가 정상적으로 실행되었습니다!: " + joinPoint.getSignature().getName());
    }

    @AfterReturning("orderController()")
    public void afterReturningLog(JoinPoint joinPoint) {
        logger.info("실행된 {} 메서드의 값이 반환되었습니다!", joinPoint.getSignature().getName());
    }

    @AfterThrowing("orderController()")
    public void afterThrowingLog(JoinPoint joinPoint) {
        logger.warn("{} 메서드에서 문제가 생겼습니다!", joinPoint.getSignature().getName());
    }
}

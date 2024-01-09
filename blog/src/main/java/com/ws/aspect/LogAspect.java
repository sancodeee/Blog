package com.ws.aspect;

import lombok.Data;
import lombok.ToString;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志处理：通过aop设置切面处理
 * <p>
 * 作用：打印请求信息
 *
 * @author wangsen
 * @date 2022/01/08
 */
@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @Pointcut("execution(* com.ws.web.*.*(..))") 定义了一个切入点，表示匹配com.ws.web包下所有类的所有方法作为切点
    @Pointcut("execution(* com.ws.web.*.*(..))")
    public void log() {
        // 该方法只是一个占位符，即便是有内容，也不会被执行
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 获取request中的url
            String url = request.getRequestURL().toString();
            // 获取客户端的IP地址
            String ip = request.getRemoteAddr();

            String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            // 获取参数列表
            Object[] args = joinPoint.getArgs();
            RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
            // 请求信息
            logger.info("Request : {}", requestLog);
        }
    }

    @After("log()")
    public void doAfter() {
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }

    /**
     * 内部类请求日志
     *
     * @author wangsen
     * @date 2022/03/08
     */
    @Data
    @ToString
    private static class RequestLog {
        private final String url;
        private final String ip;
        private final String classMethod;
        private final Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }
    }


}

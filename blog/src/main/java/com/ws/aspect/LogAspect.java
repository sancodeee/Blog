package com.ws.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志处理：通过aop设置切面处理
 *
 * @author wangsen
 * @date 2022/01/08
 */
@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.ws.web.*.*(..))")
    public void log() {
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

        @Override
        public String toString() {
            return "RequestLog{" + "url='" + url + '\'' + ", ip='" + ip + '\'' + ", classMethod='" + classMethod + '\'' + ", args=" + Arrays.toString(args) + '}';
        }
    }


}

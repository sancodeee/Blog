package com.ws.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

//控制台打印接口文档地址
@Component
@Slf4j
public class SwaggerPrintConfig implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent webServerInitializedEvent) {
        try {
            //获取ip地址
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            //获取端口号
            String port = String.valueOf(webServerInitializedEvent.getWebServer().getPort());
            //获取应用名
            String applicationName = webServerInitializedEvent.getApplicationContext().getApplicationName();
            log.info("接口文档地址：http://{}:{}/swagger-ui.html", hostAddress, port + applicationName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

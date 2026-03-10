package com.xiaorui.youyouerpsystem;

import com.xiaorui.youyouerpsystem.common.info.BaseComputerInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * @author xiaorui
 */
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.xiaorui.youyouerpsystem.mapper")
@SpringBootApplication
public class YouyouErpSystemApplication {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(YouyouErpSystemApplication.class, args);
        Environment environment = context.getBean(Environment.class);
        System.out.println("启动成功，后端服务API地址：http://" + BaseComputerInfo.getIpAddr() + ":"
                + environment.getProperty("server.port") + "/api/doc.html");
        System.out.println("您还需启动前端服务，启动命令：yarn run serve 或 npm run serve，测试用户：youyou01，密码：12345678");
    }
}

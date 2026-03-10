package com.xiaorui.youyouerpsystem.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 健康检查接口
 * @author: xiaorui
 * @date: 2026-03-05 21:56
 **/
@RequestMapping("/health")
@RestController
public class HealthCheckController {

    @RequestMapping("/check")
    public String check() {
        return "success";
    }

}

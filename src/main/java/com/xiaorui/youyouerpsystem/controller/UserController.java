package com.xiaorui.youyouerpsystem.controller;

import com.xiaorui.youyouerpsystem.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-06 22:51:08
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;





}

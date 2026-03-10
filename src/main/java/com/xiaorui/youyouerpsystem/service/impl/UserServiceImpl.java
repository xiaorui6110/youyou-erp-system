package com.xiaorui.youyouerpsystem.service.impl;

import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.mapper.UserMapper;
import com.xiaorui.youyouerpsystem.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-06 22:51:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-06 22:51:08
 */
public interface IUserService extends IService<User> {

    // ============================= 基础业务 =============================

    /**
     * 用户注册
     *
     * @param userVO 用户信息
     * @param manageRoleId 管理员角色id
     */
    void register(UserVO userVO, String manageRoleId);

    /**
     * 用户登录
     *
     * @param loginName 登录用户名
     * @param loginPassword 登录密码
     * @param request 请求对象
     * @return 用户信息
     */
    Map<String, Object> login(String loginName, String loginPassword, HttpServletRequest request);

    // ============================= 增删改查 =============================

    /**
     * 添加用户
     *
     * @param userVO 用户信息
     * @return 用户信息
     */
    UserVO addUser(UserVO userVO);

    /**
     * 删除用户
     *
     * @param userId 用户id
     * @param request 请求对象
     * @return 是否删除成功
     */
    boolean deleteUser(String userId, HttpServletRequest request);

    /**
     * 修改用户
     *
     * @param userVO 用户信息
     * @param request 请求对象
     * @return 用户信息
     */
    UserVO updateUser(UserVO userVO, HttpServletRequest request);


    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    User getCurrentUser();





}

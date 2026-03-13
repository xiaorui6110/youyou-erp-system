package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    /**
     * 用户登出
     *
     * @param request 请求对象
     * @return 是否登出成功
     */
    boolean logout(HttpServletRequest request);

    /**
     * 修改密码
     *
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param request 请求对象
     * @return 是否修改成功
     */
    String changePassword(String userId, String oldPassword, String newPassword, HttpServletRequest request);

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @param newPassword 新密码
     * @param request 请求对象
     * @return 是否重置成功
     */
    boolean resetPassword(String userId, String newPassword, HttpServletRequest request);

    /**
     * 修改用户信息
     *
     * @param userVO 用户信息
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateUserInfo(UserVO userVO, HttpServletRequest request);


    /**
     * 修改用户头像
     *
     * @param multipartFile 头像文件
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateUserAvatar(MultipartFile multipartFile, HttpServletRequest request);

    /**
     * 获取图形验证码
     *
     * @return 验证码信息
     */
    Map<String, String> getPictureVerifyCode();


    /**
     * 校验图形验证码（从登录逻辑中抽离出来）
     *
     * @param verifyCode 用户输入的验证码
     * @param serverVerifyCode 服务器存储的验证码
     * @return 是否正确
     */
    boolean checkPictureVerifyCode(String verifyCode, String serverVerifyCode);


    // TODO 微信登录相关


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
     * 批量删除用户
     *
     * @param ids 用户id列表
     * @param request 请求对象
     * @return 是否删除成功
     */
    boolean batchDeleteUser(List<String> ids, HttpServletRequest request);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateUser(User user, HttpServletRequest request);


    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    User getCurrentUser();





}

package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.UserBusiness;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 用户-角色-模块权限关系表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 14:12:41
 */
public interface IUserBusinessService extends IService<UserBusiness> {

    // ============================= 增删改查 =============================

    /**
     * 创建用户-角色-模块权限关系
     *
     * @param userBusiness 用户-角色-模块权限关系
     * @param request 请求对象
     * @return 是否创建成功
     */
    boolean createUserBusiness(UserBusiness userBusiness, HttpServletRequest request);

    /**
     * 修改用户-角色-模块权限关系
     *
     * @param userBusiness 用户-角色-模块权限关系
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateUserBusiness(UserBusiness userBusiness, HttpServletRequest request);

    /**
     * 删除用户-角色-模块权限关系
     *
     * @param userBusinessId 用户-角色-模块权限关系id
     * @return 是否删除成功
     */
    boolean deleteUserBusiness(String userBusinessId);

    /**
     * 批量删除用户-角色-模块权限关系
     *
     * @param userBusinessIds 用户-角色-模块权限关系id列表
     * @return 是否删除成功
     */
    boolean batchDeleteUserBusiness(List<String> userBusinessIds);

    /**
     * 获取用户-角色-模块权限关系
     *
     * @param userBusinessId 用户-角色-模块权限关系id
     * @return 用户-角色-模块权限关系
     */
    UserBusiness getUserBusiness(String userBusinessId);

    /**
     * 获取用户-角色-模块权限关系列表
     *
     * @param userBusiness 用户-角色-模块权限关系查询条件
     * @return 用户-角色-模块权限关系列表
     */
    List<UserBusiness> listUserBusiness(UserBusiness userBusiness);


    // ============================= 基础业务 =============================


    /**
     * 获取用户-角色-模块权限关系列表
     *
     * @param keyId 用户id/角色id/菜单id
     * @param relateType 关系类别：user-用户，role-角色，module-模块
     * @return 用户-角色-模块权限关系列表
     */
    List<UserBusiness> getBasicData(String keyId, String relateType);

    /**
     * 更新按钮权限字符串
     *
     * @param keyId 用户id/角色id/菜单id
     * @param relateType 关系类别：user-用户，role-角色，module-模块
     * @param btnStr 按钮权限字符串
     * @return 是否更新成功
     */
    boolean updateBtnStr(String keyId, String relateType, String btnStr);

    /**
     * 更新单个值
     *
     * @param keyId 用户id/角色id/菜单id
     * @param relateType 关系类别：user-用户，role-角色，module-模块
     * @param oneValue 值
     * @return 是否更新成功
     */
    boolean updateOneValueByKeyIdAndType(String keyId, String relateType, String oneValue);

}

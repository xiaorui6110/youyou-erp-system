package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.Tenant;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;

import java.util.List;

/**
 * <p>
 * 租户表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-13 20:19:45
 */
public interface ITenantService extends IService<Tenant> {

    // ============================= 增删改查 =============================

    /**
     * 创建租户
     *
     * @param userVO 租户信息
     * @return 是否创建成功
     */
    boolean createTenant(UserVO userVO);

    /**
     * 修改租户
     *
     * @param tenant 租户信息
     * @return 是否修改成功
     */
    boolean updateTenant(Tenant tenant);

    /**
     * 删除租户
     *
     * @param tenantId 租户id
     * @return 是否删除成功
     */
    boolean deleteTenant(String tenantId);

    /**
     * 批量删除租户
     *
     * @param tenantIds 租户id列表
     * @return 是否删除成功
     */
    boolean batchDeleteTenant(List<String> tenantIds);

    /**
     * 获取租户信息
     *
     * @param tenantId 租户id
     * @return 租户信息
     */
    Tenant getTenant(String tenantId);

    /**
     * 获取租户列表
     *
     * @param tenant 租户信息
     * @return 租户列表
     */
    List<Tenant> getTenantList(Tenant tenant);

    /**
     * 批量设置租户状态
     *
     * @param tenantIds 租户id列表
     * @param status 状态
     * @return 是否设置成功
     */
    boolean batchSetStatus(List<String> tenantIds, Boolean status);

}

package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.AccountItem;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 财务子表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:27
 */
public interface IAccountItemService extends IService<AccountItem> {

    // ============================= 增删改查 =============================

    /**
     * 创建账户子表
     *
     * @param accountItem 账户子表信息
     * @param request 请求对象
     * @return 是否创建成功
     */
    boolean createAccountItem(AccountItem accountItem, HttpServletRequest request);

    /**
     * 修改账户子表
     *
     * @param accountItem 账户子表信息
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateAccountItem(AccountItem accountItem, HttpServletRequest request);

    /**
     * 删除账户子表
     *
     * @param accountItemId 账户子表id
     * @return 是否删除成功
     */
    boolean deleteAccountItem(String accountItemId);

    /**
     * 批量删除账户子表
     *
     * @param accountItemIds 账户子表id列表
     * @return 是否删除成功
     */
    boolean batchDeleteAccountItem(List<String> accountItemIds);

    /**
     * 获取账户子表信息
     *
     * @param accountItemId 账户子表id
     * @return 账户子表信息
     */
    AccountItem getAccountItem(String accountItemId);

    /**
     * 获取账户子表列表
     *
     * @param accountItem 账户子表信息
     * @return 账户子表列表
     */
    List<AccountItem> getAccountItemList(AccountItem accountItem);



}

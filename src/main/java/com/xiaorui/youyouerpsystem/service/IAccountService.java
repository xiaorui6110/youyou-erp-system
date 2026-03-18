package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.Account;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 账户信息表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:05
 */
public interface IAccountService extends IService<Account> {

    // ============================= 增删改查 =============================

    /**
     * 创建账户
     *
     * @param account 账户信息
     * @param request 请求对象
     * @return 是否创建成功
     */
    boolean createAccount(Account account, HttpServletRequest request);

    /**
     * 修改账户
     *
     * @param account 账户信息
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateAccount(Account account, HttpServletRequest request);

    /**
     * 删除账户
     *
     * @param accountId 账户id
     * @return 是否删除成功
     */
    boolean deleteAccount(String accountId);

    /**
     * 批量删除账户
     *
     * @param accountIds 账户id列表
     * @return 是否删除成功
     */
    boolean batchDeleteAccount(List<String> accountIds);

    /**
     * 获取账户信息
     *
     * @param accountId 账户id
     * @return 账户信息
     */
    Account getAccount(String accountId);

    /**
     * 获取账户列表
     *
     * @param account 账户信息
     * @return 账户列表
     */
    List<Account> getAccountList(Account account);


    // ============================= 基础业务 =============================



}

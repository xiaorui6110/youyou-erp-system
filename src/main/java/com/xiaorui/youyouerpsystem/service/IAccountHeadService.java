package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.AccountHead;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 财务主表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:15
 */
public interface IAccountHeadService extends IService<AccountHead> {

    // ============================= 增删改查 =============================

    /**
     * 创建账户主表
     *
     * @param accountHead 账户主表信息
     * @param request 请求对象
     * @return 是否创建成功
     */
    boolean createAccountHead(AccountHead accountHead, HttpServletRequest request);

    /**
     * 修改账户主表
     *
     * @param accountHead 账户主表信息
     * @param request 请求对象
     * @return 是否修改成功
     */
    boolean updateAccountHead(AccountHead accountHead, HttpServletRequest request);

    /**
     * 删除账户主表
     *
     * @param accountHeadId 账户主表id
     * @return 是否删除成功
     */
    boolean deleteAccountHead(String accountHeadId);

    /**
     * 批量删除账户主表
     *
     * @param accountHeadIds 账户主表id列表
     * @return 是否删除成功
     */
    boolean batchDeleteAccountHead(List<String> accountHeadIds);

    /**
     * 获取账户主表信息
     *
     * @param accountHeadId 账户主表id
     * @return 账户主表信息
     */
    AccountHead getAccountHead(String accountHeadId);

    /**
     * 获取账户主表列表
     *
     * @param accountHead 账户主表信息
     * @return 账户主表列表
     */
    List<AccountHead> getAccountHeadList(AccountHead accountHead);



}

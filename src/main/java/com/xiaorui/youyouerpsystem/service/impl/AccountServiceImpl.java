package com.xiaorui.youyouerpsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.AccountMapper;
import com.xiaorui.youyouerpsystem.model.entity.Account;
import com.xiaorui.youyouerpsystem.service.IAccountService;
import com.xiaorui.youyouerpsystem.service.ILogService;
import com.xiaorui.youyouerpsystem.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 账户信息表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:05
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    private ILogService logService;
    @Resource
    private IUserService userService;

    // ============================= 增删改查 =============================

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean createAccount(Account account, HttpServletRequest request) {

        if (account.getInitialAmount() == null) {
            account.setInitialAmount(BigDecimal.ZERO);
        }
        List<Account> accountList = getAccountByParam(account.getAccountName(), account.getSerialNo());
        account.setIsDefault(accountList.isEmpty());
        account.setIsEnabled(true);
        try {
            save(account);
            logService.createLogWithOperation("账户",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + account.getAccountName(), request);
        } catch(Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateAccount(Account account, HttpServletRequest request) {
        try {
            updateById(account);
            logService.createLogWithOperation("账户",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + account.getAccountName(), request);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteAccount(String accountId) {
        try {

            // TODO 校验财务主表

            removeById(accountId);

        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteAccount(List<String> accountIds) {
        try {


            // TODO 校验财务主表


            removeByIds(accountIds);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    public Account getAccount(String accountId) {
        Account account = new Account();
        try {
            QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_id", accountId);
            List<Account> list = list(queryWrapper);
            if(!list.isEmpty()) {
                account = list.getFirst();
            }
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        return account;

    }

    @Override
    public List<Account> getAccountList(Account account) {
        return list(new QueryWrapper<>(account));
    }


    // ============================= 私有方法 =============================


    public List<Account> getAccountByParam(String accountName, String serialNo) {
        List<Account> list = null;
        try {
            QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", accountName);
            queryWrapper.eq("serial_no", serialNo);
            queryWrapper.ne("is_deleted", BusinessConstants.DELETE_FLAG_DELETED);
            list = list(queryWrapper);
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        return list;
    }


}

package com.xiaorui.youyouerpsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.AccountItemMapper;
import com.xiaorui.youyouerpsystem.model.entity.AccountItem;
import com.xiaorui.youyouerpsystem.service.IAccountItemService;
import com.xiaorui.youyouerpsystem.service.ILogService;
import com.xiaorui.youyouerpsystem.service.IUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 财务子表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:27
 */
@Service
public class AccountItemServiceImpl extends ServiceImpl<AccountItemMapper, AccountItem> implements IAccountItemService {

    private final Logger logger = LoggerFactory.getLogger(AccountItemServiceImpl.class);

    @Resource
    private ILogService logService;
    @Resource
    private IUserService userService;

    // ============================= 增删改查 =============================

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean createAccountItem(AccountItem accountItem, HttpServletRequest request) {
        try {
            save(accountItem);
            logService.createLogWithOperation("财务子表",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + accountItem.getAccountItemId(), request);

        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateAccountItem(AccountItem accountItem, HttpServletRequest request) {
        try {
            updateById(accountItem);
            logService.createLogWithOperation("财务子表",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + accountItem.getAccountItemId(), request);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteAccountItem(String accountItemId) {
        try {
            removeById(accountItemId);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteAccountItem(List<String> accountItemIds) {
        try {
            removeByIds(accountItemIds);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    public AccountItem getAccountItem(String accountItemId) {
        AccountItem accountItem = new AccountItem();
        try {
            QueryWrapper<AccountItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_item_id", accountItemId);
            List<AccountItem> list = list(queryWrapper);
            if(!list.isEmpty()) {
                accountItem = list.getFirst();
            }
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        return accountItem;
    }

    @Override
    public List<AccountItem> getAccountItemList(AccountItem accountItem) {
        return list(new QueryWrapper<>(accountItem));
    }
}

package com.xiaorui.youyouerpsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.AccountHeadMapper;
import com.xiaorui.youyouerpsystem.model.entity.AccountHead;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.service.IAccountHeadService;
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
 * 财务主表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-18 16:30:15
 */
@Service
public class AccountHeadServiceImpl extends ServiceImpl<AccountHeadMapper, AccountHead> implements IAccountHeadService {

    private final Logger logger = LoggerFactory.getLogger(AccountHeadServiceImpl.class);

    @Resource
    private ILogService logService;
    @Resource
    private IUserService userService;

    // ============================= 增删改查 =============================

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean createAccountHead(AccountHead accountHead, HttpServletRequest request) {
        try {
            User userInfo = userService.getCurrentUser();
            accountHead.setAccountHeadCreator(userInfo == null ? null : userInfo.getUserId());
            save(accountHead);
            logService.createLogWithOperation("财务单据",
                    BusinessConstants.LOG_OPERATION_TYPE_ADD + accountHead.getBillNo(), request);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateAccountHead(AccountHead accountHead, HttpServletRequest request) {
        try {
            updateById(accountHead);
            logService.createLogWithOperation("财务单据",
                    BusinessConstants.LOG_OPERATION_TYPE_EDIT + accountHead.getBillNo(), request);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteAccountHead(String accountHeadId) {
        try {

            // TODO 校验财务主表

            removeById(accountHeadId);

        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteAccountHead(List<String> accountHeadIds) {
        try {


            // TODO 校验财务主表


            removeByIds(accountHeadIds);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    public AccountHead getAccountHead(String accountHeadId) {
        AccountHead accountHead = new AccountHead();
        try {
            QueryWrapper<AccountHead> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_head_id", accountHeadId);
            List<AccountHead> list = list(queryWrapper);
            if (!list.isEmpty()) {
                accountHead = list.getFirst();
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return accountHead;
    }

    @Override
    public List<AccountHead> getAccountHeadList(AccountHead accountHead) {
        return list(new QueryWrapper<>(accountHead));
    }
}

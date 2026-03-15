package com.xiaorui.youyouerpsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.TenantMapper;
import com.xiaorui.youyouerpsystem.model.entity.Tenant;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;
import com.xiaorui.youyouerpsystem.service.ILogService;
import com.xiaorui.youyouerpsystem.service.ITenantService;
import com.xiaorui.youyouerpsystem.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-13 20:19:45
 */
@Slf4j
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    private final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    @Resource
    private IUserService userService;
    @Resource
    private ILogService logService;

    @Value("${youyou.manage.role-id}")
    private String manageRoleId;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean createTenant(UserVO userVO) {
        try{
            userVO.setLoginName(userVO.getLoginName());
            // 检查登录名不能重复
            userService.checkLoginName(userVO);
            userService.register(userVO, manageRoleId);
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateTenant(Tenant tenant) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                // 如果租户下的用户限制数量为1，则将该租户之外的用户全部禁用
                if (1 == tenant.getUserNumLimit()) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("tenant_id", tenant.getTenantId());
                    queryWrapper.eq("user_status", 0);
                    List<User> userList = userService.list(queryWrapper);
                    if (CollectionUtils.isNotEmpty(userList)) {
                        userList.forEach(user -> {
                            user.setUserStatus((byte) '2');
                        });
                        userService.updateBatchById(userList);
                    }
                }
                updateById(tenant);

                // TODO 更新租户对应的角色 - UserBusiness

            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteTenant(String tenantId) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                removeById(tenantId);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteTenant(List<String> tenantIds) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                removeByIds(tenantIds);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    public Tenant getTenant(String tenantId) {
        Tenant tenant = new Tenant();
        try {
            QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId);
            List<Tenant> list = list(queryWrapper);
            if(!list.isEmpty()) {
                tenant = list.getFirst();
            }
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        return tenant;
    }

    @Override
    public List<Tenant> getTenantList(Tenant tenant) {
        return list(new QueryWrapper<>(tenant));
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchSetStatus(List<String> tenantIds, Boolean status) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                String statusStr = "";
                if (status) {
                    statusStr = "批量启用";
                } else {
                    statusStr = "批量禁用";
                }
                logService.createLogWithOperation("用户",
                        BusinessConstants.LOG_OPERATION_TYPE_EDIT + tenantIds + "-" + statusStr,
                        ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
                Tenant tenant = new Tenant();
                tenant.setIsEnabled(status);
                QueryWrapper<Tenant> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("tenant_id", tenantIds);
                update(tenant, queryWrapper);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }
}

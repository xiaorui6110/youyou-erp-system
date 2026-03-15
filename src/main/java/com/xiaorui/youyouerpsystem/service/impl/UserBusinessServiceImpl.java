package com.xiaorui.youyouerpsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.UserBusinessMapper;
import com.xiaorui.youyouerpsystem.model.entity.UserBusiness;
import com.xiaorui.youyouerpsystem.service.IUserBusinessService;
import com.xiaorui.youyouerpsystem.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户-角色-模块权限关系表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 14:12:41
 */
@Slf4j
@Service
public class UserBusinessServiceImpl extends ServiceImpl<UserBusinessMapper, UserBusiness> implements IUserBusinessService {

    private final Logger logger = LoggerFactory.getLogger(UserBusinessServiceImpl.class);

    @Resource
    private IUserService userService;

    // ============================= 增删改查 =============================

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean createUserBusiness(UserBusiness userBusiness) {
        try {
            String relateValue = userBusiness.getRelateValue();
            String newRelateValue = relateValue.replaceAll(",", "][");
            newRelateValue = newRelateValue.replaceAll("\\[0]","").replaceAll("\\[]","");
            userBusiness.setRelateValue(newRelateValue);
            save(userBusiness);
            //logService.insertLog("关联关系", BusinessConstants.LOG_OPERATION_TYPE_ADD, request);
        } catch(Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateUserBusiness(UserBusiness userBusiness) {
        try{
            String relateValue = userBusiness.getRelateValue();
            String newRelateValue = relateValue.replaceAll(",", "][");
            newRelateValue = newRelateValue.replaceAll("\\[0]","").replaceAll("\\[]","");
            userBusiness.setRelateValue(newRelateValue);
            updateById(userBusiness);
            //logService.insertLog("关联关系", BusinessConstants.LOG_OPERATION_TYPE_EDIT, request);
        } catch(Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteUserBusiness(String userBusinessId) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                removeById(userBusinessId);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteUserBusiness(List<String> userBusinessIds) {
        try {
            if (BusinessConstants.DEFAULT_MANAGER.equals(userService.getCurrentUser().getLoginName())) {
                removeByIds(userBusinessIds);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    public UserBusiness getUserBusiness(String userBusinessId) {
        UserBusiness userBusiness = new UserBusiness();
        try {
            QueryWrapper<UserBusiness> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_business_id", userBusinessId);
            queryWrapper.eq("is_deleted", "0");
            List<UserBusiness> list = list(queryWrapper);
            if(!list.isEmpty()) {
                userBusiness = list.getFirst();
            }
        } catch (Exception e) {
            LoggerException.readFail(logger,e);
        }
        return userBusiness;
    }

    @Override
    public List<UserBusiness> listUserBusiness(UserBusiness userBusiness) {
        return list(new QueryWrapper<>(userBusiness));
    }

    // ============================= 基础业务 =============================


    @Override
    public List<UserBusiness> getBasicData(String keyId, String relateType) {
        List<UserBusiness> list = null;
        try{
            QueryWrapper<UserBusiness> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("key_id", keyId);
            queryWrapper.eq("relate_type", relateType);
            queryWrapper.eq("is_deleted", "0");
            list = list(queryWrapper);
        } catch (Exception e) {
            LoggerException.readFail(logger,e);
        }
        return list;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateBtnStr(String keyId, String relateType, String btnStr) {
//        logService.insertLog("关联关系",
//                new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_EDIT).append("角色的按钮权限").toString(),
//                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        UserBusiness userBusiness = new UserBusiness();
        userBusiness.setBtnStr(btnStr);
        QueryWrapper<UserBusiness> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_id", keyId);
        queryWrapper.eq("relate_type", relateType);
        queryWrapper.eq("is_deleted", "0");
        try {
            update(userBusiness, queryWrapper);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateOneValueByKeyIdAndType(String keyId, String relateType, String oneValue) {
        try {
            Map<String, String> keyIdMap = new HashMap<>();
            QueryWrapper<UserBusiness> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("relate_type", relateType);
            queryWrapper.eq("is_deleted", "0");
            List<UserBusiness> oldUbList = list(queryWrapper);
            keyIdMap.put(keyId, keyId);

            QueryWrapper<UserBusiness> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("key_id", keyId);
            queryWrapper1.eq("relate_type", relateType);
            queryWrapper1.eq("is_deleted", "0");
            List<UserBusiness> ubList = list(queryWrapper1);

            if (!ubList.isEmpty()) {
                String relateValue = ubList.getFirst().getRelateValue();
                boolean flag = relateValue.contains("[" + oneValue + "]");
                if (flag) {
                    // 存在则忽略
                } else {
                    // 不存在则追加并更新
                    relateValue = relateValue + "[" + oneValue + "]";
                    UserBusiness userBusiness = new UserBusiness();
                    userBusiness.setUserBusinessId(ubList.getFirst().getUserBusinessId());
                    userBusiness.setRelateValue(relateValue);
                    updateById(userBusiness);
                }
            } else {
                // 新增数据
                UserBusiness userBusiness = new UserBusiness();
                userBusiness.setRelateType(relateType);
                userBusiness.setKeyId(keyId);
                userBusiness.setRelateValue("[" + oneValue + "]");
                save(userBusiness);
            }
            //检查被移除的keyId
            for (UserBusiness item: oldUbList) {
                String oldValue = item.getRelateValue();
                String oldkeyId = item.getKeyId();
                if (keyIdMap.get(oldkeyId) == null) {
                    // 处理被删除的keyId
                    String relateValue = "[" + oneValue + "]";
                    if(oldValue.equals(relateValue)) {
                        // 说明value里面只有一条数据，需要进行逻辑删除
                        UserBusiness userBusiness = new UserBusiness();
                        userBusiness.setUserBusinessId(item.getUserBusinessId());
                        userBusiness.setIsDeleted("1");
                        updateById(userBusiness);
                    } else {
                        // 多条进行替换后再更新
                        String newValue = oldValue.replace(relateValue, "");
                        UserBusiness userBusiness = new UserBusiness();
                        userBusiness.setUserBusinessId(item.getUserBusinessId());
                        userBusiness.setRelateValue(newValue);
                        updateById(userBusiness);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return true;
    }
}

package com.xiaorui.youyouerpsystem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.constants.ExceptionCodeConstants;
import com.xiaorui.youyouerpsystem.common.constants.ExceptionConstants;
import com.xiaorui.youyouerpsystem.common.exception.BusinessRunTimeException;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.UserMapper;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;
import com.xiaorui.youyouerpsystem.service.IUserService;
import com.xiaorui.youyouerpsystem.utils.RedisServiceUtil;
import com.xiaorui.youyouerpsystem.utils.Tools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-06 22:51:08
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private RedisServiceUtil redisServiceUtil;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void register(UserVO userVO, String manageRoleId) {
        // 多次创建事务，事务之间无法协同，应该在入口处创建一个事务以做协调
        if(BusinessConstants.DEFAULT_MANAGER.equals(userVO.getLoginName())) {
            throw new BusinessRunTimeException(ExceptionConstants.USER_NAME_LIMIT_USE_CODE,
                    ExceptionConstants.USER_NAME_LIMIT_USE_MSG);
        } else {
            userVO.setLoginPassword(userVO.getLoginPassword());
            userVO.setIsSystem(BusinessConstants.USER_NOT_SYSTEM);
            if (userVO.getIsManager() == null) {
                userVO.setIsManager(BusinessConstants.USER_NOT_MANAGER);
            }
            userVO.setUserStatus(BusinessConstants.USER_STATUS_NORMAL);
            try {
                User user = new User();
                BeanUtils.copyProperties(userVO, user);
                save(user);
                String userId = getIdByLoginName(userVO.getLoginName());
                userVO.setUserId(userId);
            } catch(Exception e) {
                LoggerException.writeFail(logger, e);
            }
            // TODO 更新租户id
            User user = new User();
            user.setUserId(userVO.getUserId());
            user.setTenantId(userVO.getTenantId());
            //updateUserTenant(user);
            // TODO 新增用户与角色的关系

            // TODO 创建租户信息
        }
    }

    @Override
    public Map<String, Object> login(String loginName, String loginPassword, HttpServletRequest request) {

        Map<String, Object> data = new HashMap<>();
        String msgTip = "";
        User user = null;
        // 判断用户是否已经登录过，登录过不再处理
        Object userId = redisServiceUtil.getObjectFromSessionByKey(request,"userId");
        if (userId != null) {
            logger.info("==== 用户已经登录过, login 方法调用结束 ====");
            msgTip = "user already login";
        }
        //获取用户状态
        int userStatus = -1;
        try {
            redisServiceUtil.deleteObjectBySession(request,"userId");
            userStatus = validateUser(loginName, loginPassword);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>> 用户  {} 登录 login 方法 访问服务层异常 ====", loginName, e);
            msgTip = "access service exception";
        }
        // TODO 可以使用SaToken生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        switch (userStatus) {
            case ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST:
                msgTip = "user is not exist";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR:
                msgTip = "user password error";
                break;
            case ExceptionCodeConstants.UserExceptionCode.BLACK_USER:
                msgTip = "user is black";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION:
                msgTip = "access service error";
                break;
            case ExceptionCodeConstants.UserExceptionCode.BLACK_TENANT:
                msgTip = "tenant is black";
                break;
            case ExceptionCodeConstants.UserExceptionCode.EXPIRE_TENANT:
                msgTip = "tenant is expire";
                break;
            case ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT:
                msgTip = "user can login";
                // 验证通过 ，可以登录，放入session，记录登录日志
                user = getUserByLoginName(loginName);
                if (StrUtil.isNotBlank(user.getTenantId())) {
                    token = token + "_" + user.getTenantId();
                }
                redisServiceUtil.storageObjectBySession(token,"userId", user.getUserId());
                break;
            default:
                break;
        }
        data.put("msgTip", msgTip);
        if(user!=null){
            // 校验下密码是不是过于简单
            boolean pwdSimple = false;
            try {
                pwdSimple = user.getLoginPassword().equals(Tools.md5Encryp(BusinessConstants.USER_DEFAULT_PASSWORD));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            user.setLoginPassword(null);
            if (BusinessConstants.DEFAULT_MANAGER.equals(user.getLoginName())) {
                // TODO 如果是管理员，则发送登录邮件
                //sendEmailToCurrentUser(request, user);
            }
            redisServiceUtil.storageObjectBySession(token,"clientIp", Tools.getLocalIp(request));
//            logService.insertLogWithUserId(user.getUserId(), user.getTenantId(), "用户",
//                    new StringBuffer(BusinessConstants.LOG_OPERATION_TYPE_LOGIN).append(user.getLoginName()).toString(),
//                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
            data.put("token", token);
            data.put("user", user);
            data.put("pwdSimple", pwdSimple);
        }
        return data;

    }

    /**
     * 新增用户默认设置
     * 1. 是否系统自带默认为非系统自带
     * 2. 是否管理者默认为员工
     * 3. 默认用户状态为正常
     * */
    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public UserVO addUser(UserVO userVO) {

        // TODO 新增用户默认信息

        userVO.setIsSystem(BusinessConstants.USER_NOT_SYSTEM);
        if (userVO.getIsManager() == null) {
            userVO.setIsManager(BusinessConstants.USER_NOT_MANAGER);
        }
        userVO.setUserStatus(BusinessConstants.USER_STATUS_NORMAL);
        try {
            User user = new User();
            BeanUtils.copyProperties(userVO,user);
            boolean result = save(user);
            if (result) {
                return userVO;
            }
        } catch(Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return null;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteUser(String userId, HttpServletRequest request) {
        return false;
    }

    @Override
    public UserVO updateUser(UserVO userVO, HttpServletRequest request) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String userId = redisServiceUtil.getObjectFromSessionByKey(request,"userId").toString();
        return getUser(userId);
    }

    /**
     * 根据登录名获取用户id
     */
    private String getIdByLoginName(String loginName) {
        String userId = null;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", loginName);
        queryWrapper.eq("user_status", BusinessConstants.USER_STATUS_NORMAL);
        queryWrapper.ne("is_deleted", BusinessConstants.DELETE_FLAG_DELETED);
        User user = getOne(queryWrapper);
        if(user != null) {
            userId = user.getUserId();
        }
        return userId;
    }

    /**
     * 验证用户是否存在
     */
    private int validateUser(String loginName, String loginPassword) {
        // 默认是可以登录的
        List<User> list = null;
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", loginName);
            queryWrapper.ne("is_deleted", BusinessConstants.DELETE_FLAG_DELETED);
            list = list(queryWrapper);

            if (null != list && list.isEmpty()) {
                return ExceptionCodeConstants.UserExceptionCode.USER_NOT_EXIST;
            } else {
                assert list != null;
                if (list.size() == 1) {
                    if (list.getFirst().getUserStatus() != 0) {
                        return ExceptionCodeConstants.UserExceptionCode.BLACK_USER;
                    }

                    String tenantId = list.getFirst().getTenantId();
                    // TODO 校验租户信息

                }
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>访问验证用户姓名是否存在后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("login_name", loginName);
            queryWrapper.eq("login_password", loginPassword);
            queryWrapper.eq("user_status", BusinessConstants.USER_STATUS_NORMAL);
            queryWrapper.ne("is_deleted", BusinessConstants.DELETE_FLAG_DELETED);
            list = list(queryWrapper);
            if (null != list && list.isEmpty()) {
                return ExceptionCodeConstants.UserExceptionCode.USER_PASSWORD_ERROR;
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>>> 访问验证用户密码后台信息异常", e);
            return ExceptionCodeConstants.UserExceptionCode.USER_ACCESS_EXCEPTION;
        }
        return ExceptionCodeConstants.UserExceptionCode.USER_CONDITION_FIT;
    }

    /**
     * 根据登录名获取用户信息
     */
    private User getUserByLoginName(String loginName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", loginName);
        queryWrapper.eq("user_status", BusinessConstants.USER_STATUS_NORMAL);
        queryWrapper.ne("is_deleted", BusinessConstants.DELETE_FLAG_DELETED);
        List<User> list = null;
        try {
            list = list(queryWrapper);
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        User user = null;
        if (list != null && !list.isEmpty()) {
            user = list.getFirst();
        }
        return user;
    }

    /**
     * 获取用户信息
     */
    private User getUser(String userId) {
        User user = null;
        try{
            // 先校验是否登录，然后才能查询用户数据
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                    RequestContextHolder.getRequestAttributes())).getRequest();

            String currentUserId = this.getUserId(request);
            if(StrUtil.isNotBlank(currentUserId)) {
                user = getById(userId);
            }
        } catch (Exception e) {
            LoggerException.readFail(logger, e);
        }
        return user;
    }

    /**
     * 获取用户id
     */
    private String getUserId(HttpServletRequest request) {
        Object userIdObj = redisServiceUtil.getObjectFromSessionByKey(request,"userId");
        String userId = null;
        if (userIdObj != null) {
            userId = userIdObj.toString();
        }
        return userId;
    }

}

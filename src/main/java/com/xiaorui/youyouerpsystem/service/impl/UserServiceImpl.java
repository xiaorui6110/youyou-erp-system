package com.xiaorui.youyouerpsystem.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import com.xiaorui.youyouerpsystem.common.constants.ExceptionCodeConstants;
import com.xiaorui.youyouerpsystem.common.constants.ExceptionConstants;
import com.xiaorui.youyouerpsystem.common.exception.BusinessRunTimeException;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.common.response.BusinessCodeEnum;
import com.xiaorui.youyouerpsystem.mapper.UserMapper;
import com.xiaorui.youyouerpsystem.model.entity.User;
import com.xiaorui.youyouerpsystem.model.vo.UserVO;
import com.xiaorui.youyouerpsystem.service.IUserService;
import com.xiaorui.youyouerpsystem.utils.RedisUtil;
import com.xiaorui.youyouerpsystem.utils.ResponseUtil;
import com.xiaorui.youyouerpsystem.utils.Tools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private static final String SUCCESS_INFO = "操作成功";
    private static final String ERROR_INFO = "操作失败";

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    // ============================= 基础业务 =============================

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void register(UserVO userVO, String manageRoleId) {
        // 多次创建事务，事务之间无法协同，应该在入口处创建一个事务以做协调
        if (BusinessConstants.DEFAULT_MANAGER.equals(userVO.getLoginName())) {
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
            } catch (Exception e) {
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
        Object userId = redisUtil.getObjectFromSessionByKey(request,"userId");
        if (userId != null) {
            logger.info("==== 用户已经登录过, login 方法调用结束 ====");
            msgTip = "user already login";
        }
        //获取用户状态
        int userStatus = -1;
        try {
            redisUtil.deleteObjectBySession(request,"userId");
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
                redisUtil.storageObjectBySession(token,"userId", user.getUserId());
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
            redisUtil.storageObjectBySession(token,"clientIp", Tools.getLocalIp(request));
            data.put("token", token);
            data.put("user", user);
            data.put("pwdSimple", pwdSimple);
        }
        return data;

    }

    @Override
    public boolean logout(HttpServletRequest request) {
        try {
            redisUtil.deleteObjectBySession(request,"userId");
            redisUtil.deleteObjectBySession(request,"clientIp");
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }


    @Override
    public String changePassword(String userId, String oldPassword, String newPassword, HttpServletRequest request) {
        int flag = 0;
        Map<String, Object> objectMap = new HashMap<>();
        try {
            String info = "";
            User user = getUser(userId);
            // 必须和原始密码一致才可以更新密码
            if (oldPassword.equals(user.getLoginPassword())) {
                user.setLoginPassword(newPassword);
                user.setUpdateTime(LocalDateTime.now());
                updateById(user);
                // 1-成功
                flag = 1;
                info = "修改成功";
            } else {
                // 原始密码输入错误
                flag = 2;
                info = "原始密码输入错误";
            }
            objectMap.put("status", flag);
            return ResponseUtil.returnJson(objectMap, info, BusinessCodeEnum.OK.code);
        } catch (Exception e) {
            logger.error(">>>>>>>>>>>>> 修改用户ID为 ： {}密码信息失败", userId, e);
            flag = 3;
            objectMap.put("status", flag);
            return ResponseUtil.returnJson(objectMap, ERROR_INFO, BusinessCodeEnum.ERROR.code);
        }
    }

    @Override
    public boolean resetPassword(String userId, String newPassword, HttpServletRequest request) {
        User loginUser = getUser(userId);
        String loginName = loginUser.getLoginName();
        if ("admin".equals(loginName)) {
            logger.info("禁止重置超管密码");
        } else {
            User user = new User();
            user.setUserId(userId);
            user.setLoginPassword(newPassword);
            user.setUpdateTime(LocalDateTime.now());
            try{
                // 判断是否登录过
                Object loginUserId = redisUtil.getObjectFromSessionByKey(request,"userId");
                if (loginUserId != null) {
                    updateById(user);
                }
            }catch(Exception e){
                LoggerException.writeFail(logger, e);
            }
        }
        return true;
    }

    @Override
    public boolean updateUserInfo(UserVO userVO, HttpServletRequest request) {

        // TODO 修改用户及机构和用户关系

        return false;
    }

    @Override
    public boolean updateUserAvatar(MultipartFile multipartFile, HttpServletRequest request) {

        // TODO 修改用户头像，待引入minio等

        return false;
    }

    @Override
    public Map<String, String> getPictureVerifyCode() {
        // 仅包含数字的字符集
        String characters = "0123456789";
        // 生成4位数字验证码
        RandomGenerator randomGenerator = new RandomGenerator(characters, 4);
        // 定义图片的显示大小，并创建验证码对象
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(320, 100, 4, 4);
        shearCaptcha.setGenerator(randomGenerator);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        shearCaptcha.write(outputStream);
        byte[] captchaBytes = outputStream.toByteArray();
        String base64Captcha = Base64.getEncoder().encodeToString(captchaBytes);
        String captchaCode = shearCaptcha.getCode();
        // 后续使用Hutool的MD5加密（为了检查接口方便，先不加密！！！）
        //String encryptedCaptcha = DigestUtil.md5Hex(captchaCode);
        // 将加密后的验证码和Base64编码的图片存储到Redis中，设置过期时间为5分钟
        stringRedisTemplate.opsForValue().set("captcha:" + captchaCode, captchaCode, 300, TimeUnit.SECONDS);
        Map<String, String> map = new HashMap<>();
        map.put("base64Captcha", base64Captcha);
        map.put("encryptedCaptcha", captchaCode);
        return map;
    }

    @Override
    public boolean checkPictureVerifyCode(String verifyCode, String serverVerifyCode) {
        if (verifyCode != null && serverVerifyCode != null) {
            //  后续对用户输入的验证码进行MD5加密，然后与服务器存储的验证码进行比较（服务器中的存储的是MD5加密后的验证码）(也是先不加密！！！)
            //String encryptedVerifycode = DigestUtil.md5Hex(verifyCode);
            return verifyCode.equals(serverVerifyCode);
        }
        return false;
    }


    // ============================= 增删改查 =============================

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
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return null;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean deleteUser(String userId, HttpServletRequest request) {
        boolean result = false;
        StringBuilder sb = new StringBuilder();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        User user = getUser(userId);
        if (user.getUserId().equals(user.getTenantId())) {
            logger.error("异常码[{}],异常提示[{}],参数,userId:[{}]",
                    ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG,userId);
            throw new BusinessRunTimeException(ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,
                    ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG);
        }
        sb.append("[").append(user.getLoginName()).append("]");
        try {
            // 判断是否登录过
            Object loginUserId = redisUtil.getObjectFromSessionByKey(request,"userId");
            if (loginUserId != null) {
                result = removeById(userId);
                if(result) {
                    // 从redis中移除用户的登录状态
                    redisUtil.deleteObjectByUser(userId);
                }
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        if (result) {
            logger.error("异常码[{}],异常提示[{}],参数,userId:[{}]",
                    ExceptionConstants.USER_DELETE_FAILED_CODE,ExceptionConstants.USER_DELETE_FAILED_MSG,userId);
            throw new BusinessRunTimeException(ExceptionConstants.USER_DELETE_FAILED_CODE,
                    ExceptionConstants.USER_DELETE_FAILED_MSG);
        }
        return result;
    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean batchDeleteUser(List<String> ids, HttpServletRequest request) {
        boolean result = false;
        StringBuilder sb = new StringBuilder();
        sb.append(BusinessConstants.LOG_OPERATION_TYPE_DELETE);
        List<User> userList = listByIds(ids);
        for(User user: userList){
            if(user.getUserId().equals(user.getTenantId())) {
                logger.error("异常码[{}],异常提示[{}],参数,ids:[{}]",
                        ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG,ids);
                throw new BusinessRunTimeException(ExceptionConstants.USER_LIMIT_TENANT_DELETE_CODE,
                        ExceptionConstants.USER_LIMIT_TENANT_DELETE_MSG);
            }
            sb.append("[").append(user.getLoginName()).append("]");
        }
        try {
            // 判断是否登录过
            Object loginUserId = redisUtil.getObjectFromSessionByKey(request,"userId");
            if (loginUserId != null) {
                result = removeByIds(ids);
                if(result) {
                    // 从redis中移除这些用户的登录状态
                    redisUtil.deleteObjectByUser(String.valueOf(ids));
                }
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        if (result) {
            logger.error("异常码[{}],异常提示[{}],参数,ids:[{}]",
                    ExceptionConstants.USER_DELETE_FAILED_CODE,ExceptionConstants.USER_DELETE_FAILED_MSG,ids);
            throw new BusinessRunTimeException(ExceptionConstants.USER_DELETE_FAILED_CODE,
                    ExceptionConstants.USER_DELETE_FAILED_MSG);
        }
        return result;

    }

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public boolean updateUser(User user, HttpServletRequest request) {
        try {
            // 判断是否登录过
            Object userId = redisUtil.getObjectFromSessionByKey(request,"userId");
            if (userId != null) {
                saveOrUpdate(user);
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger, e);
        }
        return true;
    }

    @Override
    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        Object userId = redisUtil.getObjectFromSessionByKey(request,"userId");
        return getUser(userId.toString());
    }


    // ============================= 私有方法 =============================


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
        if (user != null) {
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
        Object userIdObj = redisUtil.getObjectFromSessionByKey(request,"userId");
        String userId = null;
        if (userIdObj != null) {
            userId = userIdObj.toString();
        }
        return userId;
    }

}

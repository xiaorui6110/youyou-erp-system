package com.xiaorui.youyouerpsystem.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaorui.youyouerpsystem.common.exception.LoggerException;
import com.xiaorui.youyouerpsystem.mapper.LogMapper;
import com.xiaorui.youyouerpsystem.model.entity.Log;
import com.xiaorui.youyouerpsystem.service.ILogService;
import com.xiaorui.youyouerpsystem.service.IUserService;
import com.xiaorui.youyouerpsystem.utils.RedisUtil;
import com.xiaorui.youyouerpsystem.utils.Tools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.xiaorui.youyouerpsystem.utils.Tools.getLocalIp;

/**
 * <p>
 * 系统操作日志表 服务实现类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 15:07:14
 */
@Slf4j
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Resource
    private IUserService userService;
    @Resource
    private RedisUtil redisUtil;


    // ============================= 增删改查 =============================


    @Override
    public boolean createLog(Log log) {
        try {
            save(log);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    public Log getLog(String logId) {
        if (StrUtil.isBlank(logId)) {
            return null;
        }
        try {
            // 提前return，不需要初始化无用对象new Log()
            return getById(logId);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
            return null;
        }
    }

    @Override
    public List<Log> getLogList(Log log) {
        return list(new QueryWrapper<>(log));
    }

    @Override
    public boolean deleteLog(String logId) {
        if (StrUtil.isBlank(logId)) {
            return false;
        }
        try {
            return removeById(logId);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
            return false;
        }
    }

    @Override
    public boolean batchDeleteLog(List<String> logIds) {
        if (CollectionUtil.isEmpty(logIds)) {
            return false;
        }
        try {
            return removeByIds(logIds);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
            return false;
        }
    }

    @Override
    public boolean updateLog(Log log) {
        try {
            updateById(log);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
        return true;
    }

    @Override
    public void createLogWithOperation(String operation, String logContent, HttpServletRequest request) {
        try {
            String userId = userService.getUserId(request);
            if (StrUtil.isNotBlank(userId)) {
                String clientIp = getLocalIp(request);
                String createTime = Tools.getNow3();
                QueryWrapper<Log> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.eq("client_ip", clientIp);
                queryWrapper.eq("create_time", createTime);
                queryWrapper.eq("operation", operation);
                long count = count(queryWrapper);

                if (count > 0) {
                    // 如果某个用户某个ip在同1秒内连续操作两遍，此时需要删除该redis记录，使其退出，防止恶意攻击
                    redisUtil.deleteObjectByUserAndIp(userId, clientIp);
                } else {
                    Log log = new Log();
                    log.setUserId(userId);
                    log.setOperation(operation);
                    log.setClientIp(getLocalIp(request));
                    log.setCreateTime(LocalDateTime.now());
                    log.setLogStatus(true);
                    log.setLogContent(logContent);
                    save(log);
                }
            }
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
    }

    @Override
    public void createLogWithUserId(String userId, String tenantId, String operation, String logContent, HttpServletRequest request) {
        try {
            Log log = new Log();
            log.setUserId(userId);
            log.setOperation(operation);
            log.setClientIp(getLocalIp(request));
            log.setCreateTime(LocalDateTime.now());
            log.setLogStatus(true);
            log.setLogContent(logContent);
            log.setTenantId(tenantId);
            save(log);
        } catch (Exception e) {
            LoggerException.writeFail(logger,e);
        }
    }
}

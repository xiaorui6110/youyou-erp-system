package com.xiaorui.youyouerpsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaorui.youyouerpsystem.model.entity.Log;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * <p>
 * 系统操作日志表 服务类
 * </p>
 *
 * @author xiaorui
 * @since 2026-03-15 15:07:14
 */
public interface ILogService extends IService<Log> {

    // ============================= 增删改查 =============================

    /**
     * 创建日志
     *
     * @param log 日志信息
     * @return 是否创建成功
     */
    boolean createLog(Log log);

    /**
     * 获取日志
     *
     * @param logId 日志id
     * @return 日志
     */
    Log getLog(String logId);

    /**
     * 获取日志列表
     *
     * @param log 日志信息
     * @return 日志列表
     */
    List<Log> getLogList(Log log);


    /**
     * 删除日志
     *
     * @param logId 日志id
     * @return 是否删除成功
     */
    boolean deleteLog(String logId);

    /**
     * 批量删除日志
     *
     * @param logIds 日志id列表
     * @return 是否删除成功
     */
    boolean batchDeleteLog(List<String> logIds);

    /**
     * 修改日志
     *
     * @param log 日志信息
     * @return 是否修改成功
     */
    boolean updateLog(Log log);

    /**
     * 创建日志
     *
     * @param operation  操作模块
     * @param logContent 内容
     * @param request    请求
     */
    void createLogWithOperation(String operation, String logContent, HttpServletRequest request);

    /**
     * 创建日志
     *
     * @param userId     用户id
     * @param tenantId   租户id
     * @param operation  操作模块
     * @param logContent 内容
     * @param request    请求
     */
    void createLogWithUserId(String userId, String tenantId, String operation, String logContent, HttpServletRequest request);

}

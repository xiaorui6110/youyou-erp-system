package com.xiaorui.youyouerpsystem.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static com.baomidou.mybatisplus.generator.config.rules.DateType.TIME_PACK;

/**
 * @description: MyBatis-Plus 代码生成器（为了避免输出目录杂乱，统一将输出文件放在 temp 目录下）
 *      <a href="https://baomidou.com/reference/new-code-generator-configuration/">...</a>
 * @author: xiaorui
 * @date: 2026-03-06 21:14
 **/

@Slf4j
public class CodeGenerator {

    /**
     * 需要生成的表名（每次修改表名生成，并修改自定义生成风格的信息）
     */
    private static final String TABLE_NAMES = "youyou_account_item";

    public static void main(String[] args) {

        // 获取数据源信息（从yaml文件中获取）
        Dict dict = YamlUtil.loadByPath("application-dev.yaml");
        Map<String, Object> dataSource = dict.getByPath("spring.datasource");
        String url = String.valueOf(dataSource.get("url"));
        String username = String.valueOf(dataSource.get("username"));
        String password = String.valueOf(dataSource.get("password"));
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("xiaorui") // 设置作者
                            .disableOpenDir() // 禁止自动打开输出目录
                            .outputDir(Paths.get(System.getProperty("user.dir")) + File.separator
                                    + "src/main/java/com/xiaorui/youyouerpsystem/generator/temp") // 输出目录
                            .dateType(TIME_PACK) // 设置时间类型策略
                            .commentDate("yyyy-MM-dd HH:mm:ss"); // 设置注释日期格式
                })
                .packageConfig(builder -> {
                    builder.parent(TABLE_NAMES) // 设置父包名（在 temp 目录下，其实没必要设置）
                            .entity("model") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mapper"); // 设置 Mapper XML 文件包名
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude(TABLE_NAMES)
                            .addTablePrefix("youyou_") // 增加过滤表前缀
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                // 使用 Freemarker 模板引擎
                .templateEngine(new FreemarkerTemplateEngine())
                .execute(); // 执行生成
        System.out.println("execute success");
    }

}

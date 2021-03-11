package cn.com.tjise.onlineedu.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置类
 */
@Configuration
@MapperScan("cn.com.tjise.onlineedu.mapper")
public class EduConfig
{
}

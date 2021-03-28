package cn.com.tjise.onlineedu.handler;

import cn.com.tjise.onlineedu.constant.GlobalConstant;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author admin
 * MyBaitsPlus 自动填充策略
 */
@Component
public class AutoInsertHandler implements MetaObjectHandler
{
    
    @Override
    public void insertFill(MetaObject metaObject)
    {
        // mybatisplus 插入拦截，首先判断当前实体是否存在 createTime 以及 updateTime 字段 以及deleted逻辑删除字段
        if (metaObject.hasSetter(GlobalConstant.CREATE_TIME))
        {
            this.strictInsertFill(metaObject, GlobalConstant.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasSetter(GlobalConstant.UPDATE_TIME))
        {
            this.strictInsertFill(metaObject, GlobalConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasSetter(GlobalConstant.DELETED))
        {
            // 插入数据时设定逻辑删除字段为0
            this.strictInsertFill(metaObject, GlobalConstant.DELETED, Integer.class, 0);
        }
        
    }
    
    @Override
    public void updateFill(MetaObject metaObject)
    {
        // mybatisplus 更新拦截，首先判断当前实体是否存在updateTime 字段
        if (metaObject.hasSetter(GlobalConstant.UPDATE_TIME))
        {
            this.strictUpdateFill(metaObject, GlobalConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }
    }
}

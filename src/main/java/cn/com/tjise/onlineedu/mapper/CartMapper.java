package cn.com.tjise.onlineedu.mapper;

import cn.com.tjise.onlineedu.entity.po.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author admin
 */
@Mapper
@Repository
public interface CartMapper extends BaseMapper<Cart>
{
    /**
     * 判断是否存在此名称的购物车表
     * @param tableName 表名
     * @return
     */
    int isExitCartTable(String tableName);
}

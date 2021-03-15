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
     * @param tableName 表名 表明格式  学生学号_cart 例如：192168151_cart
     * @return
     */
    /*@Select("SELECT count(*) FROM information_schema.`TABLES` " +
        "WHERE " +
        "TABLE_SCHEMA = 'onlineedu' " +
        "AND " +
        "TABLE_NAME = #{tableName}")
    int isExitCartTable(String tableName);*/
    
    /**
     * 创建表方法
     * @param tableName
     * @return
     */
    /*@Update("CREATE TABLE `#{tableName}` (\n" +
        "  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
        "  `class_id` varchar(255) NOT NULL COMMENT '课程id',\n" +
        "  `student_id` varchar(255) DEFAULT NULL COMMENT '教师id',\n" +
        "  `createTime` datetime NOT NULL COMMENT '创建时间',\n" +
        "  `updateTime` datetime NOT NULL COMMENT '更新时间',\n" +
        "  `deleted` int(10) DEFAULT NULL COMMENT '逻辑删除',\n" +
        "  PRIMARY KEY (`id`)\n" +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8;")
    int createCartTable(String tableName);*/
    
}

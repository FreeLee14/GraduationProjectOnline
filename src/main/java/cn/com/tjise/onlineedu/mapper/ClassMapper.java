package cn.com.tjise.onlineedu.mapper;

import cn.com.tjise.onlineedu.entity.po.Class;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Mapper
@Repository
public interface ClassMapper extends BaseMapper<Class> {

}

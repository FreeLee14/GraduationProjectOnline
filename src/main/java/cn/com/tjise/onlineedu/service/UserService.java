package cn.com.tjise.onlineedu.service;

import cn.com.tjise.onlineedu.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
public interface UserService extends IService<User>
{
    
    /**
     * 根据id查询用户权限
     * @param id
     * @return
     */
    User queryById(String id);
    
    /**
     * 根据账户号删除用户权限映射
     * @param id
     * @return
     */
    boolean deleteById(String id);
}

package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.mapper.UserMapper;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService
{
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 根据用户id查询用户权限
     *
     * @param id
     * @return
     */
    @Override
    public User queryById(String id)
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", id);
        return getOne(queryWrapper);
    }
    
    /**
     * 删除用户权限关联映射
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id)
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("u_id", id);
        return remove(queryWrapper);
    }
}

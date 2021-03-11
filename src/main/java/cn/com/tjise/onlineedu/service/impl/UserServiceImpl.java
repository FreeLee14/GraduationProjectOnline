package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.mapper.UserMapper;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

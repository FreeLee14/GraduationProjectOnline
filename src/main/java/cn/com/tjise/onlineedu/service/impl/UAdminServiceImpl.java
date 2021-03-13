package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UAdmin;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.mapper.UAdminMapper;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.util.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class UAdminServiceImpl
    extends ServiceImpl<UAdminMapper, UAdmin>
    implements UAdminService
{
    
    @Override
    public R login(UserLoginVO loginVO)
    {
        QueryWrapper<UAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq("admin_id", loginVO.getUserId());
        UAdmin admin = getOne(wrapper);
        // 短路运算先判断获取到的数据库中的密码是否存在，不存在则无法通过校验，证明用户名不存在
        if (admin.getPassword().length() != 0 && admin.getPassword().equals(loginVO.getPassword()))
        {
            // 生成token
            String token = TokenUtil.buildToken(loginVO.getUserId());
            // 组装响应的用户信息
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", admin.getName());
            data.put("age", admin.getAge());
            data.put("email", admin.getEmail());
            data.put("avatar", admin.getAvatar());
            data.put("token", token);
            return R.ok().message("管理员登录成功").data(data);
        }
        else
        {
            return R.error().message("不存在该管理员或密码不正确");
        }
    }
    
    @Override
    public Map<String, Object> info(String id)
    {
        QueryWrapper<UAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq("admin_id", id);
        UAdmin admin = getOne(wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("name", admin.getName());
        data.put("age", admin.getAge());
        data.put("email", admin.getEmail());
        data.put("avatar", admin.getAvatar());
        return data;
    }
}

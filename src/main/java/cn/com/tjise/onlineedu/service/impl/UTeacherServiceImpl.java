package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UTeacher;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.mapper.UTeacherMapper;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.util.TokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UTeacherServiceImpl
    extends ServiceImpl<UTeacherMapper, UTeacher>
    implements UTeacherService
{
    
    @Autowired
    private UserServiceImpl userService;
    
    @Override
    public R login(UserLoginVO loginVO)
    {
        QueryWrapper<UTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", loginVO.getUserId());
        UTeacher teacher = getOne(wrapper);
        // 短路运算先判断获取到的数据库中的密码是否存在，不存在则无法通过校验，证明用户名不存在
        if (teacher.getPassword().length() != 0 && teacher.getPassword().equals(loginVO.getPassword()))
        {
            // 生成token
            String token = TokenUtil.buildToken(loginVO.getUserId());
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", teacher.getName());
            data.put("age", teacher.getAge());
            data.put("email", teacher.getEmail());
            data.put("level", teacher.getLevel());
            data.put("description", teacher.getDescription());
            data.put("avatar", teacher.getAvatar());
            data.put("token", token);
            return R.ok().message("教师登录成功").data(data);
        }
        else
        {
            return R.error().message("不存在该教师或密码不正确");
        }
    }
    
    @Override
    public Map<String, Object> info(String id)
    {
        // 获取倒权限id
        User user = userService.queryById(id);
        Integer roleId = user.getRoleId();
        /**
         * 查询当前教师信息
         */
        QueryWrapper<UTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        UTeacher teacher = getOne(wrapper);
        /**
         * 组装响应体 data
         */
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", teacher.getName());
        data.put("age", teacher.getAge());
        data.put("email", teacher.getEmail());
        data.put("level", teacher.getLevel());
        data.put("description", teacher.getDescription());
        data.put("avatar", teacher.getAvatar());
        data.put("roleId", roleId);
        return data;
    }
    
    
}

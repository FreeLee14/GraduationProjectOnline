package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.mapper.UStudentMapper;
import cn.com.tjise.onlineedu.service.UStudentService;
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
public class UStudentServiceImpl
    extends ServiceImpl<UStudentMapper, UStudent>
    implements UStudentService
{
    @Autowired
    private UserServiceImpl userService;
    
    @Override
    public R login(UserLoginVO loginVO)
    {
        String userId = loginVO.getUserId();
        QueryWrapper<UStudent> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", userId);
        UStudent student = getOne(wrapper);
        // 短路运算先判断获取到的数据库中的密码是否存在，不存在则无法通过校验，证明用户名不存在
        if (student.getPassword().length() != 0 && student.getPassword().equals(loginVO.getPassword()))
        {
            // 生成token
            String token = TokenUtil.buildToken(loginVO.getUserId());
            Map<String, Object> data = info(loginVO.getUserId());
            return R.ok().message("学生登录成功").data(data).data("token", token);
        }
        else
        {
            return R.error().message("不存在该学生或密码不正确");
        }
    }
    
    /**
     * 根据id查询学生
     * @param id 学生编号
     * @return
     */
    @Override
    public Map<String, Object> info(String id)
    {
        User user = userService.queryById(id);
        Integer roleId = user.getRoleId();
        
        QueryWrapper<UStudent> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", id);
        UStudent student = getOne(wrapper);
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("roleId", roleId);
        data.put("name", student.getName());
        data.put("age", student.getAge());
        data.put("email", student.getEmail());
        data.put("school", student.getSchool());
        data.put("avatar", student.getAvatar());
        data.put("password", student.getPassword());
        return data;
    }
}

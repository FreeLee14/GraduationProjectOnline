package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UAdmin;
import cn.com.tjise.onlineedu.entity.vo.UserLoginVO;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@RestController
@CrossOrigin
@RequestMapping("/onlineedu/user")
public class UserController
{
    @Autowired
    private UserService userService;
    @Autowired
    private UAdminService uAdminService;
    @Autowired
    private UStudentService uStudentService;
    @Autowired
    private UTeacherService uTeacherService;
    
    /**
     * 登录方法
     *
     * @return
     */
    @PostMapping("login")
    public R login(@RequestBody UserLoginVO loginVO)
    {
        boolean flag = false;
        String message = "";
        
        switch (loginVO.getRoleType())
        {
            /**
             * 管理员权限
             */
            case ADMIN:
                QueryWrapper<UAdmin> wrapper = new QueryWrapper<>();
                wrapper.eq("admin_id", loginVO.getUserId());
                UAdmin admin = uAdminService.getOne(wrapper);
                // 短路运算先判断获取到的数据库中的密码是否存在，不存在则无法通过校验，证明用户名不存在
                if (admin.getPassword().length() != 0 && admin.getPassword().equals(loginVO.getPassword()))
                {
                    flag = true;
                    message = "管理员登录成功";
                }
                else
                {
                    message = "不存在该管理源或密码不正确";
                }
                break;
            /**
             * 教师权限
             */
            case TEACHER:
                break;
            /**
             * 学生权限
             */
            case STUDENT:
                break;
            default:
                break;
        }
        if (flag)
        {
            
            return R.ok().data("token", "").message(message);
        }
        else
        {
            return R.error();
        }
    }
}


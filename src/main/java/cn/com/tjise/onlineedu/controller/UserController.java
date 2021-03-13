package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        
        switch (loginVO.getRoleType())
        {
            /**
             * 管理员权限
             */
            case ADMIN:
                return uAdminService.login(loginVO);
            /**
             * 教师权限
             */
            case TEACHER:
                return uTeacherService.login(loginVO);
            /**
             * 学生权限
             */
            case STUDENT:
                return uStudentService.login(loginVO);
            default:
                /**
                 * 非以上三种权限为游客
                 */
                return R.ok().message("当前用户为游客");
        }
    }
    
    
    
    
    
}


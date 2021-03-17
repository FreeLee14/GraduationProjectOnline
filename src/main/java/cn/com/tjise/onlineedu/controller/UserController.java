package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
@Api("用户管理")
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
    
    @GetMapping("info")
    public R info(@RequestParam("userId") String userId)
    {
        User user = userService.queryById(userId);
        Integer roleId = user.getRoleId();
        
        Map<String, Object> info = null;
        if (RoleEnum.ADMIN.ordinal() + 1 == roleId)
        {
            info = uAdminService.info(userId);
            info.put("roles","[" + RoleEnum.ADMIN + "]");
        }
        else if (RoleEnum.TEACHER.ordinal() + 1 == roleId)
        {
            info = uTeacherService.info(userId);
            info.put("roles", "[" + RoleEnum.TEACHER + "]");
        }
        else if (RoleEnum.STUDENT.ordinal() + 1 == roleId)
        {
            info = uStudentService.info(userId);
            info.put("roles", "[" + RoleEnum.STUDENT + "]");
        }
        
        return R.ok().data(info);
    }
    
    
}


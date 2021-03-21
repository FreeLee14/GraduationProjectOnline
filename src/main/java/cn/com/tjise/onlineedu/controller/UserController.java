package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.service.UserService;
import cn.com.tjise.onlineedu.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "登陆")
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
    
    /**
     * 登录方法
     *
     * @return
     */
    @GetMapping("logout")
    @ApiOperation(value = "登出")
    public R logout(@RequestParam("token") String token)
    {
        boolean b = TokenUtil.validToken(token);
        if (b)
        {
            return R.ok().message("登出成功");
        }
        else
        {
            return R.error().message("登出失败");
        }
    }
    
    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("info")
    @ApiOperation("查询用户信息")
    public R info(
        @ApiParam(name = "userId", value = "用户id", required = true)
        @RequestParam("userId") String userId)
    {
        User user = userService.queryById(userId);
        Integer roleId = user.getRoleId();
        
        Map<String, Object> info = null;
        // 管理员权限
        if (RoleEnum.ADMIN.ordinal() + 1 == roleId)
        {
            info = uAdminService.info(userId);
            info.put("roles","[" + RoleEnum.ADMIN + "]");
        }
        // 教师权限
        else if (RoleEnum.TEACHER.ordinal() + 1 == roleId)
        {
            info = uTeacherService.info(userId);
            info.put("roles", "[" + RoleEnum.TEACHER + "]");
        }
        // 学生权限
        else if (RoleEnum.STUDENT.ordinal() + 1 == roleId)
        {
            info = uStudentService.info(userId);
            info.put("roles", "[" + RoleEnum.STUDENT + "]");
        }
        
        return R.ok().data(info);
    }
    
    
}


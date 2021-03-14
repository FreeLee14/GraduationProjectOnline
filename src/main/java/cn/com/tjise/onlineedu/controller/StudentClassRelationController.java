package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.StudentClassRelation;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.StudentClassRelationService;
import cn.com.tjise.onlineedu.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@RestController
@Slf4j
@RequestMapping("/onlineedu/studentClassRelation")
public class StudentClassRelationController
{
    @Autowired
    private StudentClassRelationService service;
    @Autowired
    private UserService userService;
    
    @PostMapping("save/{nowId}")
    @ApiOperation(value = "在课程中添加学生操作/学生报名操作")
    public R save(
        @PathVariable String nowId,
        @RequestBody StudentClassRelation relation)
    {
        // 获取当前用户权限
        User user = userService.queryById(nowId);
        Integer roleId = user.getRoleId();
        
        boolean save = service.save(relation);
        if (save)
        {
            log.info("success to save the relationShip from student to class!!");
            if (RoleEnum.STUDENT.ordinal() + 1 == roleId)
            {
                return R.ok().message("报名成功！！");
            }
            else if (RoleEnum.TEACHER.ordinal() + 1 == roleId)
            {
                return R.ok().message("添加学生成功！！");
            }
        }
        else
        {
            log.info("failed to save the relationShip from student to class!!");
            if (RoleEnum.STUDENT.ordinal() + 1 == roleId)
            {
                return R.error().message("报名失败！！");
            }
            else if (RoleEnum.TEACHER.ordinal() + 1 == roleId)
            {
                return R.error().message("添加学生失败！！");
            }
        }
        
        return R.error();
    }
}


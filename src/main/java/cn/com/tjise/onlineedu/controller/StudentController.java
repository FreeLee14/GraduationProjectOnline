package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/onlineedu/student")
public class StudentController
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class.getName());
    
    @Autowired
    private UStudentService service;
    @Autowired
    private UserService userService;
    
    /**
     * 注册接口
     *
     * @param student
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "学生注册接口")
    public R save(@RequestBody UStudent student)
    {
        // 保存学生信息
        boolean save = service.save(student);
        
        if (save)
        {
            // 成功存储学生信息之后，存储用户权限对应信息
            User user = new User();
            user.setRoleId(3);
            user.setUId(student.getStudentId());
            boolean flag = userService.save(user);
            if (flag)
            {
                LOGGER.info("success to save the info that it's include student and user!!");
                return R.ok();
            }
            else
            {
                LOGGER.info("failed to save the info of user!!");
                return R.error();
            }
        }
        else
        {
            LOGGER.info("failed to save the info of student!!");
            return R.error();
        }
    }
    
    @DeleteMapping("delete/{nowId}/{deleteId}")
    @ApiOperation(value = "删除学生接口")
    public R delete(
        @ApiParam(name = "nowId", value = "当前登录用户号", required = true)
        @PathVariable String nowId,
        @ApiParam(name = "deleteId", value = "要删除的目标用户号", required = true)
        @PathVariable String deleteId)
    {
        // 更具当前id查询当前用户角色
        User user = userService.queryById(nowId);
        // 当权限为1时代表管理源，有权限删除学生
        if (user.getRoleId() == 1)
        {
            UpdateWrapper<UStudent> wrapper = new UpdateWrapper<>();
            wrapper.eq("student_id", deleteId);
            // 权限通过，根据deleteId删除对应的用户
            boolean remove = service.remove(wrapper);
            // 同时删除user表中的权限关联
            boolean flag = userService.deleteById(deleteId);
            if (remove && flag)
            {
                return R.ok().message("删除成功");
            }
            else
            {
                return R.error().message("删除失败");
            }
        }
        else
        {
            return R.error().message("您没有权限删除当前用户！！");
        }
    }
    
    @PutMapping("update")
    @ApiOperation(value = "更新学生信息接口")
    public R update(@RequestBody UStudent student)
    {
        UpdateWrapper<UStudent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_id", student.getStudentId());
        boolean update = service.update(student, updateWrapper);
        
        if (update)
        {
            return R.ok().message("更新成功！！");
        }
        else
        {
            return R.ok().message("更新失败！！");
        }
    }
    
    @GetMapping("info/{id}")
    @ApiOperation(value = "根据id查询")
    public R queryById(
        @ApiParam(name = "id", value = "学生学号", required = true)
        @PathVariable String id)
    {
        Map<String, Object> data = service.info(id);
        return R.ok().data(data);
    }
}


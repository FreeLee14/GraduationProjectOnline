package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.UStudentService;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;
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
@Slf4j
@RequestMapping("/onlineedu/student")
public class StudentController
{
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
    public R save(UStudent student)
    {
        // 进行当前账号判重操作
        QueryWrapper<UStudent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", student.getStudentId());
        int count = service.count(queryWrapper);
        if (count != 0)
        {
            return R.error().message("该学生账号已存在");
        }
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
                log.info("success to save the info that it's include student and user!!");
                return R.ok();
            }
            else
            {
                log.info("failed to save the info of user!!");
                return R.error();
            }
        }
        else
        {
            log.info("failed to save the info of student!!");
            return R.error();
        }
    }
    
    @DeleteMapping("delete")
    @ApiOperation(value = "删除学生接口")
    public R delete(
        @ApiParam(name = "nowId", value = "当前登录用户号", required = true)
        @RequestParam("nowId") String nowId,
        @ApiParam(name = "deleteId", value = "要删除的目标用户号", required = true)
        @RequestParam("deleteId") String deleteId)
    {
        // 更具当前id查询当前用户角色
        User user = userService.queryById(nowId);
        // 当权限为1时代表管理源，有权限删除学生
        if (user.getRoleId() == 1)
        {
            QueryWrapper<UStudent> wrapper = new QueryWrapper<>();
            wrapper.eq("student_id", deleteId);
            /*
               1 删除对应的头像文件
             */
            // 1-1首先获取要删除的学生信息
            UStudent student = service.getOne(wrapper);
            // 1-2 获取对应的头像相对路径
            String avatar = student.getAvatar();
            // 1-3 判断是否存在路径信息
            if (avatar.length() > 0)
            {
                deleteAvatar(avatar);
            }
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
    
    
    @PostMapping("update")
    @ApiOperation(value = "更新学生信息接口")
    public R update(UStudent student)
    {
        UpdateWrapper<UStudent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_id", student.getStudentId());
        boolean update = service.update(student, updateWrapper);
        
        if (update)
        {
            log.info("success to update the info of student id is " + student.getStudentId());
            return R.ok().message("更新成功！！");
        }
        else
        {
            return R.ok().message("更新失败！！");
        }
    }
    
    @GetMapping("info")
    @ApiOperation(value = "根据id查询")
    public R queryById(
        @ApiParam(name = "id", value = "学生学号", required = true)
        @RequestParam("id") String id)
    {
        Map<String, Object> data = service.info(id);
        return R.ok().data(data);
    }
    
    @GetMapping("pageSearch")
    public R pageSearch(
        @ApiParam(name = "currentPage", value = "当前页", required = true)
        @RequestParam("currentPage") Integer currentPage,
        @ApiParam(name = "limit", value = "当前页显示记录数", required = true)
        @RequestParam("limit") Integer limit)
    {
        Page<UStudent> page = new Page<>(currentPage, limit);
        service.page(page);
        long total = page.getTotal();
        List<UStudent> records = page.getRecords();
        
        return R.ok().data("total", total).data("rows", records);
    }
    
    /**
     * 删除头像具体逻辑
     * @param avatar
     */
    private void deleteAvatar(String avatar)
    {
        // 组装头像全路径
        String avatarPath = FileUploadController.FILE_ROOT_PATH + File.separator + "user" + File.separator + "avatar" + avatar;
        // 构建文件对象
        File file = new File(avatar);
        // 如果文件存在进行删除
        file.deleteOnExit();
    }
}


package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UTeacher;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.mapper.UTeacherMapper;
import cn.com.tjise.onlineedu.service.UTeacherService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Slf4j
@CrossOrigin
@RequestMapping("/onlineedu/teacher")
public class TeacherController
{
    @Autowired
    private UTeacherService service;
    @Autowired
    private UserService userService;
    @Autowired
    private UTeacherMapper teacherMapper;
    
    /**
     * 注册接口
     *
     * @param teacher
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "老师注册接口")
    public R save(UTeacher teacher)
    {
        // 保存教师信息
        boolean save = service.save(teacher);
        
        if (save)
        {
            /*
                封装user对象
             */
            User user = new User();
            user.setRoleId(2);
            user.setUId(teacher.getTeacherId());
            boolean flag = userService.save(user);
            if (flag)
            {
                log.info("success to save the info that it's include teacher and user!!");
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
            log.info("failed to save the info of teacher!!");
            return R.error();
        }
    }
    
    @DeleteMapping("delete")
    @ApiOperation(value = "删除教师接口")
    public R delete(
        @ApiParam(name = "nowId", value = "当前登录用户号", required = true)
        @RequestParam("nowId") String nowId,
        @ApiParam(name = "deleteId", value = "要删除的目标用户号", required = true)
        @RequestParam("deleteId") String deleteId)
    {
        // 更具当前id查询当前用户角色
        User user = userService.queryById(nowId);
        // 当权限为1时代表管理员，有权限删除教师
        if (user.getRoleId() == 1)
        {
            QueryWrapper<UTeacher> wrapper = new QueryWrapper<>();
            wrapper.eq("teacher_id", deleteId);
            // 权限通过，根据deleteId删除对应的用户
            boolean remove = service.remove(wrapper);
            // 同时删除user表中的权限关联
            boolean flag = userService.deleteById(deleteId);
            if (remove && flag)
            {
                log.info("success to delete teacher who teacherId is" + deleteId );
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
    @ApiOperation(value = "更新教师信息接口")
    public R update(@RequestBody UTeacher teacher)
    {
        UpdateWrapper<UTeacher> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("teacher_id", teacher.getTeacherId());
        boolean update = service.update(teacher, updateWrapper);
        
        if (update)
        {
            return R.ok().message("更新成功！！");
        }
        else
        {
            return R.error().message("更新失败！！");
        }
    }
    
    @GetMapping("info")
    @ApiOperation(value = "根据id查询")
    public R queryById(
        @ApiParam(name = "id", value = "教师职工号", required = true)
        @RequestParam("id") String id)
    {
        Map<String, Object> data = service.info(id);
        return R.ok().data(data);
    }
    
    @GetMapping("pageSearch")
    @ApiOperation(value = "分页查询所有教师")
    public R pageSearch(
        @RequestParam("currentPage") Integer currentPage,
        @RequestParam("limit") Integer limit
        )
    {
        Page<UTeacher> page = new Page<>(currentPage, limit);
        service.page(page);
        long total = page.getTotal();
        List<UTeacher> records = page.getRecords();
        return R.ok().data("total", total).data("rows", records);
    }
    
    @GetMapping("queryAllTeacher")
    public R queryAllTeacher()
    {
        QueryWrapper<UTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("teacher_id", "name");
        List<UTeacher> teachers = teacherMapper.selectList(queryWrapper);
        
        return R.ok().data("rows", teachers);
    }
}


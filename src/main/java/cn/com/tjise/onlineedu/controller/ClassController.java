package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.entity.vo.classinfo.ClassQueryVO;
import cn.com.tjise.onlineedu.service.ClassService;
import cn.com.tjise.onlineedu.service.UTeacherService;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/onlineedu/class")
public class ClassController
{
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private UTeacherService teacherService;
    
    @ApiOperation(value = "分页查询所有课程", notes = "学生权限调用该接口")
    @GetMapping("pageSearch")
    public R pageSearch(
        @ApiParam(name = "currentPage", value = "当前页", required = true)
        @RequestParam("currentPage") Integer currentPage,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @RequestParam("limit") Integer limit)
    {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        // 按照默认的主键进行排序显示
        queryWrapper.orderByAsc("id");
        // 初始化分页查询对象，获取到的数据将封装至该对象中
        Page<Class> page = new Page<>(currentPage, limit);
        classService.page(page, queryWrapper);
        // 获取所有的记录
        List<Class> records = page.getRecords();
        // 获取记录总数
        long total = page.getTotal();
        
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("rows", records);
        
        return R.ok().data(data);
    }
    
    @ApiOperation(value = "根据教师id分页查询所有课程", notes = "教师权限查看自己课程的接口")
    @GetMapping("pageSearchByTeacher/{nowId}/{currentPage}/{limit}")
    public R pageSearchByTeacher(
        @ApiParam(name = "nowId", value = "教师id", required = true)
        @PathVariable String nowId,
        @ApiParam(name = "currentPage", value = "当前页", required = true)
        @PathVariable Integer currentPage,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Integer limit)
    {
        /*
            验证是否是教师
         */
        User user = userService.queryById(nowId);
        if (RoleEnum.TEACHER.ordinal() + 1 == user.getRoleId())
        {
            QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
            // 按照默认的主键进行排序显示
            queryWrapper.orderByAsc("id");
            // 初始化分页查询对象，获取到的数据将封装至该对象中
            Page<Class> page = new Page<>(currentPage, limit);
            classService.page(page, queryWrapper);
            // 获取所有的记录
            List<Class> records = page.getRecords();
            // 通过流式操作筛选当前教师所属课程
            List<Class> collect = records.parallelStream().filter(
                teacher -> nowId.equals(teacher.getTeacherId())
            ).collect(Collectors.toList());
            // 获取记录总数
            long total = collect.size();
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("rows", collect);
            return R.ok().data(data);
        }
        else
        {
            return R.error().message("非教师权限无法调用该分页查询接口");
        }
    }
    
    @ApiOperation(value = "根据条件分页显示课程", notes = "按条件筛选课程的接口")
    @GetMapping("pageSearchByCondition/{currentPage}/{limit}")
    public R pageSearchByCondition(
        @ApiParam(name = "currentPage", value = "当前页", required = true)
        @PathVariable Integer currentPage,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Integer limit,
        @ApiParam(name = "classQueryVO", value = "条件查询对象", required = false) // 条件实体非必填项
        @RequestBody ClassQueryVO classQueryVO
    )
    {
        // 封装page对象
        Page<Class> page = new Page<>(currentPage, limit);
        classService.pageSearchByCondition(page, classQueryVO);
        // 获取符合条件的数据
        List<Class> records = page.getRecords();
        // 获取总记录数
        long total = page.getTotal();
        // 组装响应数据data
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("rows", records);
        return R.ok().data(data);
    }
    
    
    /**
     * 新增课程，需要验证权限，仅限教师和管理员添加课程
     *
     * @param classInfo
     * @return
     */
    @PostMapping("save/{nowId}")
    @ApiOperation(value = "新增课程")
    public R saveClass(
        @ApiParam(name = "nowId", value = "当前用户id（用于判断权限）", required = true)
        @PathVariable String nowId,
        Class classInfo)
    {
        
        User user = userService.queryById(nowId);
        // 验证权限，防止接口攻击，后端进行校验
        if (RoleEnum.ADMIN.ordinal() + 1 == user.getRoleId() ||
            RoleEnum.TEACHER.ordinal() + 1 == user.getRoleId())
        {
            boolean save = classService.save(classInfo);
            
            if (save)
            {
                log.info("success to save the info of class!!");
                return R.ok().message("添加成功！！");
            }
            else
            {
                log.info("failed to save the info of class!!");
                return R.error().message("添加失败！！");
            }
        }
        else
        {
            log.info("you have no authority to insert class!!");
            return R.error().message("你没有权限新增课程");
        }
    }
    
    @DeleteMapping("delete/{nowId}/{deleteId}")
    @ApiOperation(value = "删除课程接口")
    public R delete(
        @ApiParam(name = "nowId", value = "当前登录用户号", required = true)
        @PathVariable String nowId,
        @ApiParam(name = "deleteId", value = "要删除的目标课程编号", required = true)
        @PathVariable String deleteId)
    {
        // 更具当前id查询当前用户角色
        User user = userService.queryById(nowId);
        // 仅管理员和教师可以删除课程
        if (RoleEnum.ADMIN.ordinal() + 1 == user.getRoleId() ||
            RoleEnum.TEACHER.ordinal() + 1 == user.getRoleId())
        {
            boolean flag = classService.deleteById(deleteId);
            if (flag)
            {
                log.info("success to delete class!!");
                return R.ok().message("删除成功！！");
            }
            else
            {
                log.info("failed to delete class !!");
                return R.error().message("删除失败");
            }
        }
        else
        {
            log.info("you have no authority to delete class!!");
            return R.error().message("你没有权限删除课程");
        }
    }
    
    @PutMapping("update")
    public R update(@RequestBody Class classInfo)
    {
        boolean flag = classService.updateByClassId(classInfo);
        if (flag)
        {
            log.info("success to update the info of class!!");
            return R.ok().message("更新成功！！");
        }
        else
        {
            log.info("failed to update the info of class!!");
            return R.ok().message("更新失败！！");
        }
    }
    
    /**
     * 基于课程id查询当前课程所包含学生
     * @param classId
     * @return
     */
    @GetMapping("queryStuByClassId/{classId}")
    @ApiOperation(value = "基于课程id查询当前课程所包含学生 教师权限使用该接口")
    public R queryStuByClassId(
        @ApiParam(name = "classId", value = "当前课程编号", required = true)
        @PathVariable String classId)
    {
        List<UStudent> uStudents = classService.queryStuByClassId(classId);
        
        return R.ok().data("rows", uStudents);
    }
    
    /**
     * 基于课程id查询当前课程所属老师
     * @param classId
     * @return
     */
    @GetMapping("queryTeaByClassId/{classId}")
    @ApiOperation(value = "基于课程id查询当前课程所属老师 学生权限使用该接口")
    public R queryTeaByClassId(
        @ApiParam(name = "classId", value = "当前课程编号", required = true)
        @PathVariable String classId
    )
    {
        Class classInfo = classService.queryByClassId(classId);
        String teacherId = classInfo.getTeacherId();
        Map<String, Object> info = teacherService.info(teacherId);
        return R.ok().data(info);
        
    }
    
    // 退课 （根据日期判断当前课程可以退）
    
    
    @GetMapping("info")
    public  R info(
        @ApiParam(name = "id", value = "当前课程编号", required = true)
        @RequestParam("id") String id
    )
    {
        String teacherName = "";
        Class classInfo = classService.queryByClassId(id);
        String teacherId = classInfo.getTeacherId();
        // 如果当前课程已经分配教师，则查询教师名字
        if (teacherId != null)
        {
            Map<String, Object> info = teacherService.info(teacherId);
            // 获取到教师姓名
            teacherName = (String)info.get("name");
        }
        
        
        HashMap<String, Object> data = new HashMap<>();
        data.put("classId", classInfo.getClassId());
        data.put("name", classInfo.getName());
        data.put("description", classInfo.getDescription());
        data.put("price", classInfo.getPrice());
        data.put("quota", classInfo.getQuota());
        data.put("teacherName", teacherName);
        // 将课程状态码映射为对应的文字描述
        switch (classInfo.getStatus())
        {
            case 1:
                data.put("status", "未开课");
                break;
            case 2:
                data.put("status", "已开课");
                break;
            case 3:
                data.put("status", "已结课");
                break;
            default:
                break;
        }
        return R.ok().data(data);
    }
}


package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UAdmin;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.UAdminService;
import cn.com.tjise.onlineedu.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
 * 管理员提供注册、查询信息、更改信息接口，不提供删除管理员接口
 */
@RestController
@CrossOrigin
@RequestMapping("/onlineedu/admin")
public class AdminController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class.getName());
    @Autowired
    private UAdminService service;
    @Autowired
    private UserService userService;
    
    /**
     * 注册接口
     *
     * @param admin
     * @return
     */
    @PostMapping("save")
    @ApiOperation(value = "管理员注册接口")
    public R save(@RequestBody UAdmin admin)
    {
        // 保存教师信息
        boolean save = service.save(admin);
        
        if (save)
        {
            /*
                封装user对象
             */
            User user = new User();
            user.setRoleId(1);
            user.setUId(admin.getAdminId());
            boolean flag = userService.save(user);
            if (flag)
            {
                LOGGER.info("success to save the info that it's include admin and user!!");
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
            LOGGER.info("failed to save the info of admin!!");
            return R.error();
        }
    }
    
    @PutMapping("update")
    @ApiOperation(value = "更新管理员信息接口")
    public R update(@RequestBody UAdmin admin)
    {
        UpdateWrapper<UAdmin> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("admin_id", admin.getAdminId());
        boolean update = service.update(admin, updateWrapper);
        
        if (update)
        {
            return R.ok().message("更新成功！！");
        }
        else
        {
            return R.ok().message("更新失败！！");
        }
    }
    
    @GetMapping("info")
    @ApiOperation(value = "管理员查询接口")
    public R queryById(
        @ApiParam(name = "id", value = "管理员号", required = true)
        @RequestParam("id") String id)
    {
        Map<String, Object> data = service.info(id);
        return R.ok().data(data);
    }
    
}


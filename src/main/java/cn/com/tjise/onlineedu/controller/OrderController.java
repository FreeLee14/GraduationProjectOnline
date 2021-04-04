package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.OrderEnum;
import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.OrderInfo;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.ClassService;
import cn.com.tjise.onlineedu.service.OrderInfoService;
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

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@Slf4j
@RequestMapping("/onlineedu/order")
public class OrderController
{
    
    @Autowired
    private OrderInfoService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassService classService;
    
    @PostMapping("save")
    @ApiOperation(value = "保存订单", notes = "此时订单处于未支付状态")
    public R saveOrder(OrderInfo order)
    {
        boolean flag = false;
        /*
            保存订单之前先进行当前课程的状态查询
         */
        int quota = classService.queryQuotaOfPeople(order.getClassId());
        if (quota <= 0)
        {
            return R.error().message("当前课程已没有剩余名额");
        }
        
        flag = orderService.save(order);
        
        if (flag)
        {
            log.info("success save the info of order!!");
            return R.ok().message("已保存订单，请完成支付！！");
        }
        else
        {
            log.info("failed to save the info of order!!");
            return R.error().message("保存订单失败，请重新选择！！");
        }
    }
    
    @ApiOperation(value = "修改订单状态", notes = "可修改订单状态为完成支付，废弃订单")
    @PostMapping("update")
    public R updateOrder(OrderInfo order)
    {
        UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", order.getOrderId());
        Optional.ofNullable(order.getStudentId()).ifPresent(
            studentId -> {
                updateWrapper.eq("student_id", studentId);
            }
        );
        // 使用wrapper设定更新status字段以及订单反馈feedBack字段，更新时间由拦截器进行添加
        updateWrapper.set("status", order.getStatus());
        Optional.ofNullable(order.getFeedBack()).ifPresent(
            feedBack -> updateWrapper.set("feed_back", order.getFeedBack())
        );
        boolean update = orderService.update(updateWrapper);
        
        if (update)
        {
            if (OrderEnum.PAID.ordinal() == order.getStatus())
            {
                /**
                 * 更新课程剩余名额
                 */
                Class classInfo = classService.queryByClassId(order.getClassId());
                classInfo.setQuota(classInfo.getQuota() - 1);
                classService.updateByClassId(classInfo);
                
                return R.ok().message("付款成功！！");
            }
            else if (OrderEnum.IGNORE.ordinal() == order.getStatus())
            {
                return R.ok().message("废弃订单成功！！");
            }
        }
        else
        {
            if (OrderEnum.PAID.ordinal() == order.getStatus())
            {
                return R.error().message("付款失败！！");
            }
            else if (OrderEnum.IGNORE.ordinal() == order.getStatus())
            {
                return R.error().message("废弃订单失败！！");
            }
        }
        return R.error();
    }
    
    @PostMapping("applyReturnOrder")
    public R applyReturnOrder(OrderInfo order)
    {
        UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", order.getOrderId());
        Optional.ofNullable(order.getStudentId()).ifPresent(
            studentId -> {
                updateWrapper.eq("student_id", studentId);
            }
        );
        // 使用wrapper设定更新status字段以及订单反馈feedBack字段，更新时间由拦截器进行添加
        updateWrapper.set("status", order.getStatus());
        Optional.ofNullable(order.getFeedBack()).ifPresent(
            feedBack -> updateWrapper.set("feed_back", order.getFeedBack())
        );
        boolean update = orderService.update(updateWrapper);
        if (update)
        {
            return R.ok().message("申请成功");
        }
        else 
        {
            return R.error().message("申请失败");
        }
    }
    
    @ApiOperation(value = "修改订单状态", notes = "可修改订单状态为完成支付，废弃订单")
    @PostMapping("updateFeedback")
    public R updateFeedback(OrderInfo order)
    {
        UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", order.getOrderId());
        Optional.ofNullable(order.getStudentId()).ifPresent(
            studentId -> {
                updateWrapper.eq("student_id", studentId);
            }
        );
        // 使用wrapper设定更新status字段以及订单反馈feedBack字段，更新时间由拦截器进行添加
        updateWrapper.set("feed_back", order.getFeedBack());
        // 如果同意退订申请，此时需要再更新订单状态为已废弃
        if (order.getFeedBack() == 2)
        {
            updateWrapper.set("status", 2);
        }
        boolean update = orderService.update(updateWrapper);
        
        if (update)
        {
            return R.ok().message("更新成功");
        }
        else
        {
            return R.error().message("更新失败");
        }
        
    }
    
    @ApiOperation(value = "删除订单")
    @DeleteMapping("delete")
    public R deleteByOrderId(
        @RequestParam("orderId") String orderId,
        @RequestParam("nowId") String nowId
    )
    {
        if (isStudent(nowId))
        {
            QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            queryWrapper.eq("student_id", nowId);
            boolean remove = orderService.remove(queryWrapper);
            if (remove)
            {
                return R.ok().message("订单删除成功！！");
            }
            else
            {
                return R.error().message("订单删除失败！！");
            }
        }
        else
        {
            return R.error().message("当前用户没有权限执行此操作！！");
        }
    }
    
    @GetMapping("pageSearch")
    @ApiOperation(value = "依据当前的学生id进行分页查询")
    public R pageSearch(
        @ApiParam(name = "nowId", value = "当前学生id", required = true)
        @RequestParam("nowId") String nowId,
        @ApiParam(name = "currentPage", value = "当前页码", required = true)
        @RequestParam("currentPage") Integer currentPage,
        @ApiParam(name = "limit", value = "当前页数记录数", required = true)
        @RequestParam("limit") Integer limit
    )
    {
        Page<OrderInfo> page = new Page<>(currentPage, limit);
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        /*
            判断当前id是否为学生，后端进行权限校验
         */
        if (isStudent(nowId))
        {
            // 查询当前学生id的订单信息
            queryWrapper.eq("student_id", nowId);
            orderService.page(page, queryWrapper);
            // 获取总记录数
            long total = page.getTotal();
            // 获取所有记录
            List<OrderInfo> records = page.getRecords();
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("rows", records);
            return R.ok().data(data);
        }
        // 管理员权限可以查看所有的订单
        else if (isAdmin(nowId))
        {
            orderService.page(page);
            // 获取总记录数
            long total = page.getTotal();
            // 获取所有记录
            List<OrderInfo> records = page.getRecords();
            Long collect = records.stream().filter(
                orderInfo -> orderInfo.getFeedBack() == 1
            ).count();
            
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("rows", records);
            data.put("feedBackNumber", collect);
            return R.ok().data(data);
        }
        
        return R.error().message("当前用户没有权限查询订单");
    }
    
    
    @GetMapping("info")
    @ApiOperation(value = "根据订单编号获取订单具体信息")
    public R info(
        @ApiParam(name = "id", value = "订单编号", required = true)
        @RequestParam("id") String id,
        @ApiParam(name = "nowId", value = "当前学生id", required = true)
        @RequestParam("nowId") String nowId
    )
    {
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        // 如果是学生，就按照订单号以及学生账号进行确认具体订单信息
        if (isStudent(nowId))
        {
            queryWrapper.eq("order_id", id);
            queryWrapper.eq("student_id", nowId);
        }
        // 如果是管理员则只需要根据订单号进行确定唯一订单，但是这里存在缺陷，如果订单号不唯一将出现问题
        else if (isAdmin(nowId))
        {
            queryWrapper.eq("order_id", id);
        }
        else
        {
            return R.error().message("您当前没有权限查询当前订单信息");
        }
        OrderInfo order = orderService.getOne(queryWrapper);
        Class classInfo;
        assert order.getClassId() != null;
        classInfo = classService.queryByClassId(order.getClassId());
        
        HashMap<String, Object> data = new HashMap<>();
        data.put("orderId", order.getOrderId());
        data.put("studentId", order.getStudentId());
        data.put("classId", classInfo.getClassId());
        data.put("className", classInfo.getName());
        data.put("createTime", order.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("updateTime", order.getUpdateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        data.put("status", order.getStatus());
        data.put("price", classInfo.getPrice());
        // 添加订单反馈字段
        data.put("feedBack", order.getFeedBack());
        
        return R.ok().data(data);
        
    }
    
    @GetMapping("feedbackNumber")
    @ApiOperation(value = "查询所有订单反馈处于申请退订状态的订单")
    public R getFeedBackNumber(
        @ApiParam(name = "currentPage", value = "当前页码", required = true)
        @RequestParam("currentPage") Integer currentPage,
        @ApiParam(name = "limit", value = "当前页数记录数", required = true)
        @RequestParam("limit") Integer limit
    )
    {
        Page<OrderInfo> page = new Page<>(currentPage, limit);
        // 分页查询所有feedback == 1 的订单 
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("feed_back", 1);
        orderService.page(page, queryWrapper);
        long total = page.getTotal();
        List<OrderInfo> records = page.getRecords();
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("rows", records);
        return R.ok().data(data);
    }
    
    /**
     * 验证是否为学生权限的方法抽取
     *
     * @param nowId
     * @return
     */
    private boolean isStudent(String nowId)
    {
        boolean flag = false;
        // 根据当前用户号查询用户
        User user = userService.queryById(nowId);
        // 判断用户权限
        if (RoleEnum.STUDENT.ordinal() + 1 == user.getRoleId())
        {
            flag = true;
        }
        
        return flag;
    }
    
    /**
     * 验证是否是管理员方法抽离
     *
     * @param nowId
     * @return
     */
    private boolean isAdmin(String nowId)
    {
        boolean flag = false;
        
        User user = userService.queryById(nowId);
        
        if (RoleEnum.ADMIN.ordinal() + 1 == user.getRoleId())
        {
            flag = true;
        }
        
        return flag;
    }
}


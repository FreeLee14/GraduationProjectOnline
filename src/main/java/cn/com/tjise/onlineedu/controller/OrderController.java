package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.OrderEnum;
import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.Order;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.service.ClassService;
import cn.com.tjise.onlineedu.service.OrderService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
@RequestMapping("/onlineedu/order")
public class OrderController
{
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassService classService;
    
    @PostMapping("save")
    @ApiOperation(value = "保存订单", notes = "此时订单处于未支付状态")
    public R saveOrder(Order order)
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
    
        if (order != null)
        {
            flag = orderService.save(order);
        }
        
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
    @PutMapping("update")
    public R updateOrder(@RequestBody Order order)
    {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", order.getOrderId());
        // 使用wrapper设定更新status字段，更新时间由拦截器进行添加
        updateWrapper.set("status", order.getStatus());
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
    
    @ApiOperation(value = "删除订单")
    @DeleteMapping("delete/{orderId}")
    public R deleteByOrderId(@PathVariable String orderId)
    {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        
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
    
    @GetMapping("pageSearch/{nowId}/{currentPage}/{limit}")
    @ApiOperation(value = "依据当前的学生id进行分页查询")
    public R pageSearch(
        @ApiParam(name = "nowId", value = "当前学生id", required = true)
        @PathVariable String nowId,
        @ApiParam(name = "currentPage", value = "当前页码", required = true)
        @PathVariable Integer currentPage,
        @ApiParam(name = "limit", value = "当前页数记录数", required = true)
        @PathVariable Integer limit
    )
    {
        /*
            判断当前id是否为学生，后端进行权限校验
         */
        User user = userService.queryById(nowId);
        if (RoleEnum.STUDENT.ordinal() + 1 == user.getRoleId())
        {
            Page<Order> page = new Page<>(currentPage, limit);
            orderService.page(page);
            // 获取总记录数
            long total = page.getTotal();
            // 获取所有记录
            List<Order> records = page.getRecords();
    
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
            data.put("rows", records);
            
            return R.ok().data(data);
        }
        else
        {
            return R.error().message("当前用户没有权限查询订单");
        }
        
    }
}


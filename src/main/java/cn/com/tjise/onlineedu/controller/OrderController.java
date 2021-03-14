package cn.com.tjise.onlineedu.controller;


import cn.com.tjise.onlineedu.constant.OrderEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Order;
import cn.com.tjise.onlineedu.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/onlineedu/order")
public class OrderController
{
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping("save")
    @ApiOperation(value = "保存订单", notes = "此时订单处于未支付状态")
    public R saveOrder(@RequestBody Order order)
    {
        boolean flag = false;
        
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
    
    @ApiOperation(value = "修改订单状态")
    @PutMapping("update/{orderId}/{status}")
    public R updateOrder(
        @ApiParam(name = "orderId", value = "订单id", required = true)
        @PathVariable String orderId,
        @ApiParam(name = "status", value = "订单状态", required = true)
        @PathVariable Integer status
    )
    {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_id", orderId);
        // 使用wrapper设定更新status字段，更新时间由拦截器进行添加
        updateWrapper.set("status", status);
        boolean update = orderService.update(updateWrapper);
        
        if (update)
        {
            if (OrderEnum.PAID.ordinal() == status)
            {
                return R.ok().message("付款成功！！");
            }
            else if (OrderEnum.IGNORE.ordinal() == status)
            {
                return R.ok().message("废弃订单成功！！");
            }
        }
        else
        {
            if (OrderEnum.PAID.ordinal() == status)
            {
                return R.error().message("付款失败！！");
            }
            else if (OrderEnum.IGNORE.ordinal() == status)
            {
                return R.error().message("废弃订单失败！！");
            }
        }
        return R.error();
    }
    
    // 删除订单
    
    // 查询订单
}


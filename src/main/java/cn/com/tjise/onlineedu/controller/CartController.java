package cn.com.tjise.onlineedu.controller;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Cart;
import cn.com.tjise.onlineedu.service.CartService;
import cn.com.tjise.onlineedu.service.ClassService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: onlineedu
 * @description: 购物车前端控制器
 * @author: admin
 * @create: 2021-03-15 19:40
 **/

@RestController
@Slf4j
@RequestMapping("/onlineedu/cart")
public class CartController
{
    
    @Autowired
    private CartService cartService;
    @Autowired
    private ClassService classService;
    
    @PostMapping("save")
    @ApiOperation(value = "保存至购物车")
    public R save(@RequestBody Cart cart)
    {
        /*
            保存订单之前先进行当前课程的状态查询
         */
        int quota = classService.queryQuotaOfPeople(cart.getClassId());
        if (quota <= 0)
        {
            return R.error().message("当前课程已没有剩余名额");
        }
        // 如果有名额，可以保存至购物车
        boolean save = cartService.save(cart);
        
        if (save)
        {
            log.info("success to save cart!!");
            return R.ok().message("成功保存购物车！！");
        }
        else
        {
            log.info("failed to save cart!!");
            return R.ok().message("保存购物车失败！！");
        }
    }
    
    // 删除购物车（批量删除）
    
}

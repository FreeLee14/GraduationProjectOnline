package cn.com.tjise.onlineedu.controller;

import cn.com.tjise.onlineedu.constant.RoleEnum;
import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.Cart;
import cn.com.tjise.onlineedu.entity.po.Class;
import cn.com.tjise.onlineedu.entity.po.User;
import cn.com.tjise.onlineedu.mapper.ClassMapper;
import cn.com.tjise.onlineedu.service.CartService;
import cn.com.tjise.onlineedu.service.ClassService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: onlineedu
 * @description: 购物车前端控制器
 * @author: admin
 * @create: 2021-03-15 19:40
 **/

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/onlineedu/cart")
public class CartController
{
    
    @Autowired
    private CartService cartService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassMapper classMapper;
    
    @PostMapping("save")
    @ApiOperation(value = "保存购物车")
    public R save(Cart cart)
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
    
    /**
     * 分页查询所有购物车信息
     *
     * @param nowId
     * @param currentPage
     * @param limit
     * @return
     */
    @GetMapping("pageSearch")
    public R pageSearch(
        @ApiParam(name = "nowId", value = "当前学生id", required = true)
        @RequestParam("nowId") String nowId,
        @ApiParam(name = "currentPage", value = "当前页码", required = true)
        @RequestParam("currentPage") Integer currentPage,
        @ApiParam(name = "limit", value = "当前页数记录数", required = true)
        @RequestParam("limit") Integer limit
    )
    {
        List<Class> classes = new ArrayList<>();
        /*
         *  首先进行验证是否是学生权限
         */
        User user = userService.queryById(nowId);
        // 匹配当前权限是学生权限
        if (RoleEnum.STUDENT.ordinal() + 1 == user.getRoleId())
        {
            Page<Cart> page = new Page<>(currentPage, limit);
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
            // 匹配学生id查询购物车
            queryWrapper.eq("student_id", nowId);
            cartService.page(page, queryWrapper);
            long total = page.getTotal();
            List<Cart> records = page.getRecords();
            // 转换成classId集合
            if (records.size() != 0)
            {
                List<String> classIds = records.parallelStream().map(Cart::getClassId).collect(Collectors.toList());
                QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
                // 设定范围查询
                classQueryWrapper.in("class_id", classIds);
                classes = classMapper.selectList(classQueryWrapper);
            }
            return R.ok().data("total", total).data("rows", classes);
        }
        else
        {
            return R.error();
        }
    }
    
    /**
     * 删除逻辑
     *
     * @param nowId
     * @param classId
     * @return
     */
    @DeleteMapping("delete")
    public R delete(
        @ApiParam(name = "nowId", value = "当前学生id", required = true)
        @RequestParam("nowId") String nowId,
        @ApiParam(name = "classId", value = "课程id", required = true)
        @RequestParam("classId") String classId
    )
    {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        queryWrapper.eq("student_id", nowId);
        boolean remove = cartService.remove(queryWrapper);
        
        if (remove)
        {
            return R.ok().message("成功移出购物车！！");
        }
        else
        {
            return R.error().message("移出购物车失败！！");
        }
    }
}

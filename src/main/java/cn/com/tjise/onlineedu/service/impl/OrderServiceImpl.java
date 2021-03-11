package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.po.Order;
import cn.com.tjise.onlineedu.mapper.OrderMapper;
import cn.com.tjise.onlineedu.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}

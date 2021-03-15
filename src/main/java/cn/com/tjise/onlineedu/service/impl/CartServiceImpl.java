package cn.com.tjise.onlineedu.service.impl;

import cn.com.tjise.onlineedu.entity.po.Cart;
import cn.com.tjise.onlineedu.mapper.CartMapper;
import cn.com.tjise.onlineedu.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService
{
}

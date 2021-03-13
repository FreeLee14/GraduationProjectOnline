package cn.com.tjise.onlineedu.service;

import cn.com.tjise.onlineedu.entity.dto.R;
import cn.com.tjise.onlineedu.entity.po.UStudent;
import cn.com.tjise.onlineedu.entity.vo.user.UserLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author admin
 * @since 2021-03-11
 */
public interface UStudentService extends IService<UStudent>
{
    /**
     * 登录
     * @param loginVO
     * @return
     */
    R login(UserLoginVO loginVO);
    
    /**
     * 查询用户信息
     * @param id
     * @return
     */
    Map<String, Object> info(String id);
    
}

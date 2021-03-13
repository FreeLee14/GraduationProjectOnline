package cn.com.tjise.onlineedu.handler;

import cn.com.tjise.onlineedu.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admins
 * 过滤器，用于验证token的有效性
 */
@Component
public class TokenHandler implements HandlerInterceptor
{
    /**
     * 定义日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class.getName());
    
    /**
     * 进去controller之前的拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String token = request.getHeader("token");
        // 验证token
        if (!TokenUtil.validToken(token))
        {
            // 设定token失效的响应状态码
            response.setStatus(203);
            // 重定向到默认路径（登录页面）
            response.sendRedirect("/");
            response.getWriter().print("{\"msg\":\"token is expired!!\"}");
            LOGGER.info("token is expired !!");
            return false;
        }
        return true;
    }
}

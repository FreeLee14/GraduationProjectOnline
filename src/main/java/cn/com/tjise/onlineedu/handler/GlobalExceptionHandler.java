package cn.com.tjise.onlineedu.handler;

import cn.com.tjise.onlineedu.entity.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author admin
 * 全局异常拦截器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    /**
     * 所有发生的异常都会走到这里，并进行逻辑操作,将异常堆栈信息输出到日志中，同时返回客户端错误信息
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e)
    {
        // 通过日志输出异常信息
        log.info(e.getMessage());
        e.printStackTrace();
        return R.error();
    }
}

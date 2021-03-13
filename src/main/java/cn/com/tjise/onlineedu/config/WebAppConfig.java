package cn.com.tjise.onlineedu.config;

import cn.com.tjise.onlineedu.handler.TokenHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurationSupport
{
    /**
     * 定义当前全局变量 排除路径集合
     */
    private List<String> excludePathPatternList;
    
    /**
     * 注册自定义的拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry)
    {
        // 添加我们的拦截器，并通过链式调用addPathPatterns添加拦截的路径规则（/** 代表拦截所有氢气） excludePathPatterns 排除拦截
        registry.addInterceptor(new TokenHandler()).addPathPatterns("/**").excludePathPatterns(getUrls());
        super.addInterceptors(registry);
    }
    
    /**
     * 定义需要避免过滤的url
     *
     * @return list
     */
    private List<String> getUrls()
    {
        List<String> list = new ArrayList<>();
        list.add("/");
        list.add("/onlineedu");
        list.add("/onlineedu/user");
        list.add("/index");
        list.add("/static/**");
        list.add("/static/user/css/*.css");
        list.add("/static/user/js/*.js");
        list.add("/static/user/images/*");
        this.setExcludePathPatternList(list);
        return excludePathPatternList;
    }
    
    public void setExcludePathPatternList(List<String> excludePathPatternList)
    {
        this.excludePathPatternList = excludePathPatternList;
    }
}

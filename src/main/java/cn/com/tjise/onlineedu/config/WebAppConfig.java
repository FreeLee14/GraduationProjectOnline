package cn.com.tjise.onlineedu.config;

import cn.com.tjise.onlineedu.handler.TokenHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
    
    @Bean
    public TokenHandler getTokenHandler()
    {
        return new TokenHandler();
    }
    
    /**
     * 注册自定义的拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry)
    {
        // 添加我们的拦截器，并通过链式调用addPathPatterns添加拦截的路径规则（/** 代表拦截所有氢气） excludePathPatterns 排除拦截
        registry.addInterceptor(getTokenHandler()).addPathPatterns("/**").excludePathPatterns(getUrls());
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
        list.add("/onlineedu/user/login");
        list.add("/onlineedu/user/info");
        list.add("/onlineedu/upload/userAvatar");
        list.add("/index");
        list.add("/user/*/**");
        list.add("/static/user/css/*.css");
        list.add("/static/user/js/*.js");
        list.add("/static/user/images/*");
        list.add("/swagger-ui.html/**");
        list.add("/swagger-resources/**");
        list.add("/webjars/**");
        list.add("/v2/**");
        this.setExcludePathPatternList(list);
        return excludePathPatternList;
    }
    
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    public void setExcludePathPatternList(List<String> excludePathPatternList)
    {
        this.excludePathPatternList = excludePathPatternList;
    }
    
    
}

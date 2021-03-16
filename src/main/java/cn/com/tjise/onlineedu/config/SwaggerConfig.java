package cn.com.tjise.onlineedu.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: online_parent
 * @description: swagger配置类
 * @author: admin
 * @create: 2021-01-19 11:53
 **/

@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    @Bean
    public Docket webApiConfig()
    {
        
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("webApi")
            .apiInfo(webApiInfo())
            .select()
            .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .build();
        
    }
    
    private ApiInfo webApiInfo()
    {
        
        return new ApiInfoBuilder()
            .title("在线教育API文档")
            .description("本文档描述了在想教育平台所有接口")
            .version("1.0")
            .contact(new Contact("admin", "http://onlineedu.com", "1484840359@qq.com"))
            .build();
    }
    
}

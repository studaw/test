package com.blog.config;



import com.blog.intercepter.AdminInterceptor;
import com.blog.intercepter.GlobalIntercepter;
import com.blog.intercepter.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Administrator
 * @program: xfg
 * @description: 全局配置
 * @create: 2019-12-14 11:21
 */
@Configuration
public class GlobalConfig implements WebMvcConfigurer {

    /**
     * 让GlobalIntercepter提前加载，否则 在GlobalIntercepter 里面使用 @Autowired 会注入失败
     *
     * @return
     */
    @Bean
    public HandlerInterceptor getAdminInterceptor() {
        return new AdminInterceptor();
    }
    @Bean
    public HandlerInterceptor getUserInterceptor(){
        return new UserInterceptor();
    }

    /**
     * 添加全局拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //管理员拦截
        registry.addInterceptor(getAdminInterceptor()).addPathPatterns("/hzh/**")
                .excludePathPatterns("/hzh/login", "/hzh/logout","/hzh/adminLogin");

//        user拦截
        registry.addInterceptor(getUserInterceptor()).addPathPatterns("/user/**");
    }
}

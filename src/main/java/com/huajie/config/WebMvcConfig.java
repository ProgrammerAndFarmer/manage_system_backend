package com.huajie.config;

import com.huajie.utils.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //虚拟路径来映射文件的真实绝对路径(完美的隐藏了文件的真实物理路径，可以说不但很安全，而且外网可以直接访问)
//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("backend/**").addResourceLocations("classpath:backend/");
//        registry.addResourceHandler("front/**").addResourceLocations("classpath:front/");
//    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Converters running...");
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置转换器对象，底层使用 Jackson 将 Java 对象转换为 Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到 mvc 框架的转换器集合当中去
        converters.add(0, messageConverter);//0 代表转换器索引，数字越小优先级越高
    }
}

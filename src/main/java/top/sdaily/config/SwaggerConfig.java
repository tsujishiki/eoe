package top.sdaily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by soya on 2016/10/28.
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket eoeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("eoe")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/eoe/*.*"))//过滤的接口
                .build()
                .apiInfo(eoeApiInfo());
    }

    private ApiInfo eoeApiInfo() {
        ApiInfo apiInfo = new ApiInfo("EOE API",//大标题
                "eoe ........",//小标题
                "1.0",//版本
                "NO terms of service",
                "dshsun@gmail.com ",//作者
                "",//链接显示文字
                ""//网站链接
        );

        return apiInfo;
    }
}

package com.guCoding.carrotMarket.config;

import com.guCoding.carrotMarket.config.auth.LoginUser;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(List.of(this.apiKey(), this.refreshTokenKey()))
                .ignoredParameterTypes(LoginUser.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.guCoding.carrotMarket"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()));
    }


    // JWT SecurityContext 구성
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("ACCESS_TOKEN", authorizationScopes), new SecurityReference("REFRESH_TOKEN", authorizationScopes));
    }


    // swagger-ui/index.html

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Carrot-market")
                .description("당근마켓 기능구현")
                .version("3.0.0")
                .build();
    }

    // 요청헤더, 매개변수이름, 위치
    private ApiKey apiKey() {
        return new ApiKey("ACCESS_TOKEN", "ACCESS_TOKEN", "header");
    }

    private ApiKey refreshTokenKey() {
        return new ApiKey("REFRESH_TOKEN", "REFRESH_TOKEN", "header");
    }


}

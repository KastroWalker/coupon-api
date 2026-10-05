package dev.kastro.coupon_api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI couponOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Coupon API")
                .description("API REST para criação, consulta e deleção (soft delete) de cupons de desconto.")
                .version("v1"));
    }
}

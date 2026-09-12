
package moinammaoueni.kmtech.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kmTechOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KMTech API")
                        .description(
                                "API de la plateforme KMTech dédiée à "
                                + "l'écosystème technologique comorien."
                        )
                        .version("1.0.0"));
    }
}


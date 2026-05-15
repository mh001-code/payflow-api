package com.payflow.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PayFlow API")
                        .version("1.0.0")
                        .description("""
                                API REST de pagamentos simplificados entre usuários.

                                **Regras de negócio:**
                                - Usuários do tipo COMUM podem transferir e receber
                                - Usuários do tipo LOJISTA apenas recebem
                                - Toda transferência consulta um autorizador externo
                                - Notificação assíncrona é enviada após cada transferência aprovada
                                """)
                        .contact(new Contact()
                                .name("Márcio Henrique")
                                .email("marcioincode@gmail.com")));
    }
}

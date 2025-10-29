package mdp.tirexchageservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("TIR Exchange Service API")
                        .description("Сервис обмена сообщениями МДП (имитация интеграции с IRU)")
                        .version("1.0"));
    }
}


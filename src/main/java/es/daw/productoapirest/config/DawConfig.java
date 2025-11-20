package es.daw.productoapirest.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config.daw")
@Data
@NoArgsConstructor
public class DawConfig {
    private String code;
    private String message;
}

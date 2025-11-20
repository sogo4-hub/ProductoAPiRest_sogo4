package es.daw.productoapirest.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:mi-config.properties", encoding = "UTF-8")
@EnableConfigurationProperties(DawConfig.class)
public class AppConfig {
    // No necesita contenido: su función es registrar la configuración
}

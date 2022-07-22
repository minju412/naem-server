package naem.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource(value = "classpath:secret.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties(prefix = "redis")
@Getter
@Setter
public class SecretYamlRead {

    private String host;
    private String port;

}

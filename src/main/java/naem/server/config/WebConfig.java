package naem.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOrigins(
                "http://localhost:3000",
                "https://localhost:3000",
                "http://localhost:8080",
                "https://localhost:8080",
                "https://main.d3dkvptyooc3y2.amplifyapp.com/",
                "http://192.168.0.108:8080"
            )
            .allowedMethods("*")
            .allowedHeaders("*")
            .maxAge(3600);
    }
}

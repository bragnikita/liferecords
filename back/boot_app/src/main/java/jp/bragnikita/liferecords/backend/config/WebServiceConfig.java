package jp.bragnikita.liferecords.backend.config;

import jp.bragnikita.liferecords.backend.controllers.utils.PathWildcardParameterResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebServiceConfig {

    @Value("${app.timeline.storage.root}")
    private String storageLocation;

    @Bean
    public WebMvcConfigurer applicationWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
            }

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new PathWildcardParameterResolver());
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String attachmentsDir;
                if (Path.of(storageLocation).isAbsolute()) {
                    attachmentsDir = storageLocation;
                } else {
                    attachmentsDir = Paths.get(storageLocation).toAbsolutePath().toString();
                }
                if (!attachmentsDir.endsWith("/"))
                    attachmentsDir = attachmentsDir + "/";
                registry.addResourceHandler("/resources/**")
                        .addResourceLocations("file:" + attachmentsDir)
                        .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
            }
        };
    }

}

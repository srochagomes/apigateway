package br.com.rd.conf;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.resource.VersionResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebFlux
public class WebConfig implements WebFluxConfigurer {


    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {
        configurer
                .setUseCaseSensitiveMatch(true)
                .setUseTrailingSlashMatch(false)
                .addPathPrefix("/",
                        HandlerTypePredicate.forAnnotation(RestController.class));
    }
}
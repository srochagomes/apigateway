package com.api.apigateway.conf;

import com.api.apigateway.conf.securitypath.ApiGatewaySecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:''}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:''}")
    private String issuerUri;


    @Autowired
    private ObjectMapper mapper;

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http, ApiGatewaySecurity apiGatewaySecurity, GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler) {

        log.info("Security configuration {} ",this.transformJSON(apiGatewaySecurity));

        ServerHttpSecurity securityHttp = http.csrf().disable();
        ServerHttpSecurity.AuthorizeExchangeSpec configure = apiGatewaySecurity.configure(securityHttp.authorizeExchange());
        ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtSpec = configure.and().oauth2ResourceServer().accessDeniedHandler(globalErrorWebExceptionHandler).jwt().jwtDecoder(jwkDecoder())
                .jwtAuthenticationConverter(jwtMonoAuthenticationConverter());

        return jwtSpec.and().authenticationEntryPoint(globalErrorWebExceptionHandler).accessDeniedHandler(globalErrorWebExceptionHandler).and().build();

    }

    @Bean
    public ReactiveJwtDecoder jwkDecoder() {

        var jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).jwsAlgorithm(SignatureAlgorithm.RS512).build();
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtIssuerValidator(issuerUri),
                new JwtTimestampValidator()));
        return jwtDecoder;
    }


    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtMonoAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }


    private String transformJSON(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

}

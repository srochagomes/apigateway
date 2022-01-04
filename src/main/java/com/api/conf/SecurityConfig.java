package com.api.conf;

import com.api.conf.securitypath.ApiGatewaySecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
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
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
                new RoleVoter(),
                new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtMonoAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }

    @Bean
    public ReactiveJwtDecoder jwkDecoder() {

        var jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtIssuerValidator(issuerUri),
                new JwtTimestampValidator()));
        return jwtDecoder;
    }

    private String transformJSON(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

}

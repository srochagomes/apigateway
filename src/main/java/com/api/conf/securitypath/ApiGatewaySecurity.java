package com.api.conf.securitypath;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="apigateway.security")
@Data
public class ApiGatewaySecurity {

    private ConfigPath configs;

    public ServerHttpSecurity.AuthorizeExchangeSpec configure(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {

        if (this.configs.hasPathMatchers()){
            authorizeExchangeSpec = this.configs.configPathMatchers(authorizeExchangeSpec);
        }

        if (this.configs.hasAnyExchange()){
            authorizeExchangeSpec = this.configs.configAnyExchange(authorizeExchangeSpec);
        }

        return authorizeExchangeSpec;
    }
}

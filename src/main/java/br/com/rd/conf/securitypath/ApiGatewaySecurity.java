package br.com.rd.conf.securitypath;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="apigateway.security")
@Data
public class ApiGatewaySecurity {

    private ConfigPath configs;

    public void configure(ServerHttpSecurity httpSecurity) {

        if (this.configs.hasPathMatchers()){
            this.configs.configPathMatchers(httpSecurity);
        }

        if (this.configs.hasAnyExchange()){
            this.configs.configAnyExchange(httpSecurity);
        }
    }
}

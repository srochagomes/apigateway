package com.api.conf.securitypath;

import lombok.Data;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.Optional;

@Data
public class PathMatcher extends Matcher{

    @Override
    public ServerHttpSecurity.AuthorizeExchangeSpec configure(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange) {
        Boolean permitAllAccess = Optional.ofNullable(this.getPermitAll()).orElseGet(() -> Boolean.FALSE);

        if(permitAllAccess){
            return authorizeExchange.pathMatchers(this.methodSelected(),
                    this.getUrlPattern()).permitAll();
        }else{
            if (this.hasMethodSelected()){
                return authorizeExchange.pathMatchers(this.methodSelected(),
                        this.getUrlPattern()).hasAnyRole(this.getRoles());
            }
            return authorizeExchange.pathMatchers(
                    this.getUrlPattern()).hasAnyRole(this.getRoles());

        }
    }
}

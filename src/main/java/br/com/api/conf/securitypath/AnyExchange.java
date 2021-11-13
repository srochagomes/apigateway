package br.com.api.conf.securitypath;

import lombok.Data;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.Optional;

@Data
public class AnyExchange extends Matcher{

    @Override
    public ServerHttpSecurity.AuthorizeExchangeSpec configure(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange) {
        Boolean permitAllAccess = Optional.ofNullable(this.getPermitAll()).orElseGet(() -> Boolean.FALSE);

        if(permitAllAccess){
            return  authorizeExchange.anyExchange().permitAll();
        }else{
            return authorizeExchange.anyExchange().hasAnyRole(this.getRoles());
        }
    }
}

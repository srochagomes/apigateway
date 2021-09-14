package br.com.rd.conf.securitypath;

import lombok.Data;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import java.util.Optional;

@Data
public class AnyExchange extends Matcher{

    @Override
    public void configure(ServerHttpSecurity httpSecurity) {
        Boolean permitAllAccess = Optional.ofNullable(this.getPermitAll()).orElseGet(() -> Boolean.FALSE);

        if(permitAllAccess){
            httpSecurity.authorizeExchange().anyExchange().permitAll();
        }else{
            httpSecurity.authorizeExchange().anyExchange().hasAnyRole(this.getRoles());
        }
    }
}

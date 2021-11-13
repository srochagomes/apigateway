package br.com.api.conf.securitypath;

import lombok.Data;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
public class ConfigPath {

    private PathMatcher[] pathMatchers;
    private AnyExchange anyExchange;

    public boolean hasAnyExchange() {
        return Objects.nonNull(anyExchange);
    }

    public boolean hasPathMatchers() {
        return !ObjectUtils.isEmpty(pathMatchers);
    }

    public ServerHttpSecurity.AuthorizeExchangeSpec configAnyExchange(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange) {
        return this.getAnyExchange().configure(authorizeExchange);
    }

    public ServerHttpSecurity.AuthorizeExchangeSpec configPathMatchers(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchange) {
        for(PathMatcher pathMatcherItem : this.getPathMatchers()){
            authorizeExchange = pathMatcherItem.configure(authorizeExchange);
        }

        return authorizeExchange;
    }
}

package br.com.rd.conf.securitypath;

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
        return !ObjectUtils.isEmpty(anyExchange);
    }

    public void configAnyExchange(ServerHttpSecurity httpSecurity) {
        this.getAnyExchange().configure(httpSecurity);
    }

    public void configPathMatchers(ServerHttpSecurity httpSecurity) {
        for(PathMatcher pathMatcherItem : this.getPathMatchers()){
            pathMatcherItem.configure(httpSecurity);
        }
    }
}

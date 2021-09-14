package br.com.rd.conf.securitypath;

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.util.ObjectUtils;

@Data
public class Matcher {

    private String method;
    private String patterns;
    private Boolean permitAll;
    private String[] roles;

    public HttpMethod methodSelected(){
        if (ObjectUtils.isEmpty(this.method)){
            return null;
        }

        return HttpMethod.resolve(this.method);
    }

    public void configure(ServerHttpSecurity authorizeExchange) {
    }
}

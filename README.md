# GatewayWebFlux

Este projeto fornece um **API Gateway** construído sobre o ecossistema Spring, incluindo: Spring 5, Spring Boot 2 e
Project Reactor. O Spring Cloud Gateway tem como objetivo fornecer uma maneira simples, mas eficaz de direcionar para
APIs e fornecer questões transversais a elas, como: segurança, monitoramento / métricas e resiliência.

## Documentação de Referência/Guia

Como material de apoio, favor considerar as seguintes fontes de informação:

- [Spring Cloud Gateway - Spring.io](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Gateway - Spring Cloud Project](https://cloud.spring.io/spring-cloud-gateway/reference/html/)

# Configurando o Resource Server e o endpoint

O resource server precisa saber onde pode encontrar as chaves públicas para validar a autenticidade do token de acesso
que foi fornecido. O endpoint é configurado no application.yml para cada aplicativo:

```
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
            issuer-uri: https://url/auth/realms/example
            jwk-set-uri: https://url/auth/realms/example/protocol/openid-connect/certs
```

# Configurando as rotas

Path        | Tipo        | Description
:---------: | :------: | :-------:
id            | String    | ID da rota.
uri        | String    | URI da rota.
order        | Number    | Ordem da rota.
predicates    | Array    | A coleção de predicados de rota. Cada item define o nome e os argumentos de um determinado predicado

#### Segue o exemplo abaixo:

```
cloud:  
  gateway:  
    routes:  
      - id: ms-service  
        uri: http://localhost:8888/v1/api/stocks  
        order: 10  
        predicates:  
          - Path=/v1/api/stocks/**
```

# Configurando as roles para as rotas

Path        | Tipo        | Description
:---------: | :------: | :-------:
method        | String    | Método HTTP (Get, Post, Put, Delete)
urlPattern    | String    | Url do método
permitAll    | Boolean    | TRUE para permitir acesso sem autorização das roles, sendo necessário deletar o atributo "roles" também. FALSE para exigir autorização por roles, sendo necessário incluir o campo "roles" e seu respectivo valor.
roles        | Array    | A coleção de roles da rota
anyExchange    | Array        | Qualquer exchange fora das citadas, exige o uso da role que deve ser atribuída

#### Segue os exemplos abaixo:

```
apigateway:  
  security:  
    configs:  
      pathMatchers:  
        - method: POST  
          urlPattern: '/*/*/*/oauth2/token'  
          permitAll: true  
  
        - method: POST  
          urlPattern: "/*/*/*/stocks/*"  
          roles:  
            - admin  
            - user  
            - operator
  
        - method: GET  
          urlPattern: "/*/*/*/stocks/*"  
          roles:  
            - admin  
            - user  
            
        - method: DELETE
          urlPattern: "/*/*/*/stocks/*"  
          roles:  
            - admin
  
      anyExchange:  
        roles:  
          - admin  
          - user
          - operator
```
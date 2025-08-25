package com.market_place.gateway_service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomGlobalFilter implements GlobalFilter {

    private final WebClient.Builder webClient;

    /// Invio il token al microservice auth-service per validarlo ed estrarre i dati dell'utente
    /// Inserisco i dati nell' header della richiesta, in tal modo ogni microservice gestirà questi a suo piacimento
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[GATEWAY] Global filter in esecuzione");

        ServerHttpRequest request =  exchange.getRequest();
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange); // o blocchi la request
        }

        return webClient.build()
                .get()
                .uri("http://AUTH-SERVICE/api/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .exchangeToMono(response -> {
                    if(!response.statusCode().is2xxSuccessful()){
                        return Mono.error(new RuntimeException("Token non valido"));
                    }

                    HttpHeaders headers = response.headers().asHttpHeaders();
                    log.info("Header ricevuti dal microservice {}", headers);

                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", headers.getFirst("X-User-Id"))
                            .header("X-User-Email", headers.getFirst("X-User-Email"))
                            .header("X-User-Roles", headers.getFirst("X-User-Roles"))
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }
}

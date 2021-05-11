package edu.am.reactor.amreactor.handler.sample;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FluxSampleHandler {

    public Mono<ServerResponse> fluxStringStream(ServerRequest serverRequest){

        Flux<String> stringStream = Flux.just("A","B","C","D");
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(stringStream, String.class);
    }

    public Mono<ServerResponse> fluxIntStream(ServerRequest serverRequest){

        Flux<Integer> stringStream = Flux.just(1,2,3,4);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(stringStream, Integer.class);
    }
}

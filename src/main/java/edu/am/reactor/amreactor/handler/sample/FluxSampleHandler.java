package edu.am.reactor.amreactor.handler.sample;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class FluxSampleHandler {

    /*
     Different strings are always concatenated into one
     https://github.com/spring-projects/spring-framework/issues/20807
     https://github.com/reactor/reactor-netty/issues/589
    */
    public Mono<ServerResponse> fluxStringStream(ServerRequest serverRequest){

        Flux<String> stringStream = Flux.just("A","B","C","D");
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(stringStream, String.class);

        // Returns:  ABCD
    }


    public Mono<ServerResponse> fluxListStream(ServerRequest serverRequest){

        Flux<String> stringStream = Flux.just("A","B","C","D");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(stringStream.collectList(), List.class);

        // Returns: ["A","B","C","D"]
    }

    public Mono<ServerResponse> fluxIntStream(ServerRequest serverRequest){

        Flux<Integer> intStream = Flux.just(1,2,3,4);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(intStream, Integer.class);

        // Returns : [1,2,3,4]
    }
}

package edu.am.reactor.amreactor.router.sample;

import edu.am.reactor.amreactor.handler.sample.FluxSampleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class SampleRouterFunction {

    @Bean
    public RouterFunction<ServerResponse> fluxStreamRoute(FluxSampleHandler fluxSampleHandler){

        return RouterFunctions.route(GET("/route/fluxStringStream").and(accept(MediaType.APPLICATION_JSON)), fluxSampleHandler::fluxStringStream)
                .andRoute(GET("/route/fluxIntStream").and(accept(MediaType.APPLICATION_JSON)), fluxSampleHandler::fluxIntStream);
    }
}

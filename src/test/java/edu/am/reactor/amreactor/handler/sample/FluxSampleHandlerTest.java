package edu.am.reactor.amreactor.handler.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
/*Cannot use @WebFluxTest as it doesn't scan @Component annotation (FluxSampleHandler class)*/
public class FluxSampleHandlerTest {

    @Autowired
    WebTestClient testClient;


    @Test
    public void fluxStringStreamTest(){

        Flux<String> intStream = testClient.get().uri("/route/fluxStringStream")    //Prepare an HTTP GET request.
                .accept(MediaType.TEXT_PLAIN)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .returnResult(String.class)    // Exit the chained flow in order to consume the response body externally,
                .getResponseBody();     // Return the response body as a Flux<T> of decoded elements.

        StepVerifier.create(intStream.log())
                .expectSubscription()
                .expectNext("ABCD")
                .verifyComplete();
    }


    @Test
    public void fluxListStreamTest(){

       // Mono<String> streamList = Mono.just("A","B","C","D");

        testClient.get().uri("/route/fluxListStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_JSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
            //    .returnResult(List.class)    // Exit the chained flow in order to consume the response body externally,
            //    .getResponseBody();     // Return the response body as a Flux<T> of decoded elements.
                .expectBodyList(String.class)
                .consumeWith((response) -> {
                    System.out.println(response.getResponseBody().get(0));
                    assertThat(response.getResponseBody().get(0).contains("A"));
                });

/*
        StepVerifier.create(listStream.log())
                .expectSubscription()
               // .expectNext(streamList)
                .expectNext(streamList)
                .verifyComplete();
*/

    }

    @Test
    public void fluxIntStreamTest(){


        Flux<Integer>  intStream = testClient.get().uri("/route/fluxIntStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_JSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .returnResult(Integer.class)    // Exit the chained flow in order to consume the response body externally,
                .getResponseBody();

        StepVerifier.create(intStream.log())
                .expectSubscription()
                .expectNext(1,2,3,4)
                .verifyComplete();
    }

}

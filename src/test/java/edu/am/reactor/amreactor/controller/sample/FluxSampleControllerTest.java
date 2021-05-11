package edu.am.reactor.amreactor.controller.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
/*      https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/reactive/WebFluxTest.html
        WebFluxTest Annotation that can be used for a Spring WebFlux test that focuses only on Spring WebFlux components.
        Using this annotation will disable full auto-configuration and instead apply only configuration relevant
        to WebFlux tests (i.e. @Controller, @ControllerAdvice, @JsonComponent, Converter/GenericConverter,
        and WebFluxConfigurer beans but not @Component, @Service or @Repository beans).*/
@AutoConfigureWebTestClient(timeout = "8000") // Counter to Error : Timeout on blocking read for 5000 MILLISECONDS
public class FluxSampleControllerTest {

/*  By default, tests annotated with @WebFluxTest will also auto-configure a WebTestClient.
    Client for testing web servers that uses WebClient internally to perform requests while
    also providing a fluent API to verify responses*/
    @Autowired
    WebTestClient testClient;

    @Test
    public void getIntStreamTest_Method1(){

        Flux<Integer> intStream = testClient.get().uri("/fluxIntStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .returnResult(Integer.class)    // Exit the chained flow in order to consume the response body externally,
                .getResponseBody();     // Return the response body as a Flux<T> of decoded elements.

        StepVerifier.create(intStream)
                .expectSubscription()
                .expectNext(1,2,3,4,5,6,7)
                .verifyComplete();
    }

    @Test
    public void getIntStreamTest_Method2(){

        List<Integer> expectedList = List.of(1,2,3,4,5,6,7);

        EntityExchangeResult<List<Integer>> listResults = testClient.get().uri("/fluxIntStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();    // Exit the chained flow in order to consume the response body externally,

        assertEquals(expectedList,listResults.getResponseBody());

        // Used @AutoConfigureWebTestClient(timeout = "8000") As it was getting Error : Timeout on blocking read for 5000 MILLISECONDS
    }

    @Test
    public void getIntStreamTest_Method3(){

        List<Integer> expectedList = List.of(1,2,3,4,5,6,7);

        testClient.get().uri("/fluxIntStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expectedList, response.getResponseBody());
                });

        // Used @AutoConfigureWebTestClient(timeout = "8000") As it was getting Error : Timeout on blocking read for 5000 MILLISECONDS
    }

    @Test
    public void getIntStream_infiniteTest(){

        Flux<Long> intStream = testClient.get().uri("/fluxInfiniteStream")    //Prepare an HTTP GET request.
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()            // API call happens here, Perform the exchange without a request body.
                .expectStatus().isOk()
                .returnResult(Long.class)    // Exit the chained flow in order to consume the response body externally,
                .getResponseBody();     // Return the response body as a Flux<T> of decoded elements.

        StepVerifier.create(intStream)
                .expectSubscription()
                .expectNext(0L,1L,2L,3L,4L)
                .thenCancel()
                .verify();
    }
}

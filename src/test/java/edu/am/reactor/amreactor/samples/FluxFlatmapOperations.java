package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxFlatmapOperations {

    @Test
    public void fluxWithFlatMap(){
        Flux<String> studFlux = Flux.fromIterable(Arrays.asList("Tima","Jerry","Green","Vincent")) // Flux<String>
                                    .flatMap(s -> {
                                        return Flux.fromIterable(getData(s)); // Flux<String> for each element
                                    }) // used to get data from database and Create flux of each return
                                    .log();

        studFlux.subscribe(System.out::println);

    }

    private List<String> getData(String s) {
        int id =0;
        id = (int) (Math.random()*100);

        System.out.println("data:"+s);
        return Arrays.asList(String.valueOf(id),s);
    }


    // Following method will lose input order
    @Test
    public void fluxWithFlatMap_parallel(){
       Flux<String> studFlux = Flux.fromIterable(Arrays.asList("Tima","Jerry","Green","Vincent")) // Flux<String>
                .window(2) // Flux<Flux<String>> --> (Tima,Jerry) To make group of 2
                .flatMapSequential(s -> s.map(this::getData).subscribeOn(parallel()) ) // Flux<List<String>> To perform operation in parallel
                .flatMap(s -> Flux.fromIterable(s)) // Flux<String> for each element
                .log();

       // https://stackoverflow.com/questions/52375697/flux-not-subscribing-in-spring-5-reactor
        studFlux.blockLast(); // Subscribe to this Flux and block indefinitely until the upstream signals its last value or completes.

/*        StepVerifier.create(studFlux)
                .expectNextCount(8)
                .verifyComplete();*/

    }



    @Test
    public void fluxWithFlatMap_parallel_InOrder(){
        Flux<String> studFlux = Flux.fromIterable(Arrays.asList("Tima","Jerry","Green","Vincent")) // Flux<String>
                .window(2) // Flux<Flux<String>> --> (Tima,Jerry) To make group of 2
                // concatMap is slower than flatMapSequential but maintains the order
                .concatMap(s -> s.map(this::getData).subscribeOn(parallel())) // Flux<List<String>> To perform operation in parallel
                .flatMap(s -> Flux.fromIterable(s)) // Flux<String> for each element
                .log();

        //  studFlux.subscribeOn(parallel()).subscribe(System.out::println);
        StepVerifier.create(studFlux)
                .expectNextCount(8)
                .verifyComplete();

    }

    public static class FluxErrorHandling {

        @Test
        public void fluxErrorHandling_Using_OnErrorResume(){

            Flux<String> fluxS = Flux.just("Scott","Dina","Sharon")
                    .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                    .concatWithValues("Green")
                    .onErrorResume(e -> {
                        System.out.println("Error Name: "+e);
                        return Flux.just("Error Default value");    // this value will be sent on subscribe
                    })
                    //Error Name: java.lang.RuntimeException: Error Occurred
                    //09:43:13.438 [main] INFO reactor.Flux.OnErrorResume.1 - onNext(Error Default value)
                    .log();

            fluxS.blockLast();
        }

        @Test
        public void fluxErrorHandling_Using_OnErrorReturn(){

            Flux<String> fluxS = Flux.just("Scott","Dina","Sharon")
                    .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                    .concatWithValues("Green")
                    .onErrorReturn("Default Error value")  // this value will be sent on subscribe
                     //09:45:58.708 [main] INFO reactor.Flux.OnErrorResume.1 - onNext(Sharon)
                    //09:45:58.708 [main] INFO reactor.Flux.OnErrorResume.1 - onNext(Default Error value)
                    .log();

            fluxS.blockLast();
        }

        @Test
        public void fluxErrorHandling_WithRetry(){

            Flux<String> fluxS = Flux.just("Scott","Dina","Sharon")
                    .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                    .concatWithValues("Green")              // Will not be subscribed
                    .onErrorMap(e -> new RuntimeException(e))  // this value will be sent on subscribe
                    //09:45:58.708 [main] INFO reactor.Flux.OnErrorResume.1 - onNext(Sharon)
                    //09:45:58.708 [main] INFO reactor.Flux.OnErrorResume.1 - onNext(Default Error value)
                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(3)))
                    .log();

            fluxS.blockLast();
        }
    }

    public static class FluxSampleClassTest {

        @Test
        public void fluxTestSample1(){
            StepVerifier.create(Flux.just("testing","flux","with","data").log())
                        .expectNext("testing")
                        .expectNext("flux")
                        .expectNext("with")
                        .expectNext("data")
                        .verifyComplete();
        }
    }
}

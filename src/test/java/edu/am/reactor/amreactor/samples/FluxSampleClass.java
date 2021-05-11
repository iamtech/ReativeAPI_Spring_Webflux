package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxSampleClass {

    @Test
    public void fluxSample(){

        Flux<String> fluxS = Flux.just("I","Am","testing","flux data")
                            .concatWith(Flux.error( new RuntimeException("Custom Exception")))
                            .log();
        fluxS.subscribe(System.out::println,
                System.out::println); // second argument for exception
    }

    // Creating flux from Collections/ Iterables
    @Test
    public void fluxUsingItrables(){
        List<String> names = Arrays.asList("new","data","for","flux");

        Flux<String> namesFlux = Flux.fromIterable(names).log();
        namesFlux.subscribe(System.out::println);

    }

    @Test
    public void fluxUsingArrays(){
        String[] values = new String[]{"new","data","from","Arrays"};

        Flux<String> namesFlux = Flux.fromArray(values).log();
        namesFlux.subscribe(System.out::println);

    }

    @Test
    public void fluxUsingStream(){
        List<String> names = Arrays.asList("new","data","for","flux");

        Flux<String> namesFlux = Flux.fromStream(names.stream()).log();
        namesFlux.subscribe(System.out::println);

    }

    @Test
    public void fluxUsingRange(){

        Flux<Integer> namesFlux = Flux.range(1, 5).log();

        namesFlux.subscribe(System.out::println);

    }

}

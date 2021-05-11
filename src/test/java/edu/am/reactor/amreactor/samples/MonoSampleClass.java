package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class MonoSampleClass {

    @Test
    public void monoSample(){
        Mono<String> monoS = Mono.just("Only Element").log();

        monoS.subscribe(System.out::println);
    }

    @Test
    public void monoUsingJustOREmpty(){
        Mono<String> monoS = Mono.justOrEmpty(null);    // Mono.Empty()

        monoS.subscribe(System.out::println);
    }

    @Test
    public void monoUsingSupplier(){
        Mono<String> monoS = Mono.fromSupplier(new Supplier<String>() {
            @Override
            public String get() {
                return "from supplier";
            }
        });

        monoS.subscribe(System.out::println);
    }
}

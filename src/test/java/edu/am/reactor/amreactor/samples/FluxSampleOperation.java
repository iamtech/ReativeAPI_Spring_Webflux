package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxSampleOperation {

    @Test
    public void fluxWithFilter(){
        Flux<String> names = Flux.just("tima","kay","John","Shawn")
                                .filter(s -> s.startsWith("J")).log();
        names.subscribe(System.out::println);
    }

    @Test
    public void fluxConvertUpper(){
        Flux<String> names = Flux.just("tima","kay","John","Shawn")
                .map(s -> s.toUpperCase()).log();
        names.subscribe(System.out::println);
    }

    @Test
    public void fluxConvertTOLength(){
        Flux<Integer> names = Flux.just("tima","kay","John","Shawn")
                .map(s -> s.length()).log();
        names.subscribe(System.out::println);
    }

    @Test
    public void fluxConvertTOLength_repeat(){
        Flux<Integer> names = Flux.just("tima","kay","John","Shawn")
                .map(s -> s.length()).
                        repeat(1); // Repeat the OnNext for each Elements
        names.subscribe(System.out::println);
    }

    @Test
    public void fluxConvertUpperWithFliter(){
        Flux<String> names = Flux.just("tima","kay","John","Shawn")
                .filter(s -> s.length()>4)
                .map(s -> s.toUpperCase()).log();
        names.subscribe(System.out::println);
    }

    @Test
    public void combineFluxUsingMerge(){
        Flux<String> fluxS1 = Flux.just("A","B","C");
        Flux<String> fluxS2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.merge(fluxS1,fluxS2).log();

        mergedFlux.blockLast();
    }

    @Test
    public void combineFluxUsingMerge_WithDelay(){
        Flux<String> fluxS1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxS2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        // Loses the order
        Flux<String> mergedFlux = Flux.merge(fluxS1,fluxS2).log();

        mergedFlux.blockLast();
    }


    @Test
    public void combineFluxUsingConcat_WithDelay_WithOrder(){
        Flux<String> fluxS1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxS2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        // Maintains the order but takes more time
        Flux<String> mergedFlux = Flux.concat(fluxS1,fluxS2).log();

        mergedFlux.blockLast();
    }

    @Test
    public void combineFluxUsingZip(){
        Flux<String> fluxS1 = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> fluxS2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        // Zip multiple sources together, that is to say wait for all the sources to
        // emit one element and combine these elements once into an output value (constructed by the provided combinator).
        Flux<String> mergedFlux = Flux.zip(fluxS1,fluxS2, (s1,s2) -> {  // make combinations of [AD] , [BE], [CF]
            return s1.concat(s2);
        }).log();

        mergedFlux.blockLast();

       /* Output:
        09:29:20.293 [main] INFO reactor.Flux.Zip.1 - onSubscribe(FluxZip.ZipCoordinator)
        09:29:20.293 [main] INFO reactor.Flux.Zip.1 - request(unbounded)
        09:29:21.396 [parallel-2] INFO reactor.Flux.Zip.1 - onNext(AD)
        09:29:22.417 [parallel-4] INFO reactor.Flux.Zip.1 - onNext(BE)
        09:29:23.422 [parallel-2] INFO reactor.Flux.Zip.1 - onNext(CF)
        09:29:23.422 [parallel-2] INFO reactor.Flux.Zip.1 - onComplete()*/
    }
}

package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxInfiniteStream {

    @Test
    public void  fluxInfinite(){
        Flux<Long> fluxl = Flux.interval(Duration.ofSeconds(1))
                .take(10)   // 0..9
                .log();

        fluxl.blockLast();
    }

    @Test
    public void  fluxInfinite_WithDelay(){
        Flux<Integer> fluxI = Flux.interval(Duration.ofSeconds(1))
                .map( l -> Integer.valueOf(l.intValue()))
                .delayElements(Duration.ofSeconds(1))
                .take(10)   // 0..9
                .log();

        fluxI.blockLast();
    }
}

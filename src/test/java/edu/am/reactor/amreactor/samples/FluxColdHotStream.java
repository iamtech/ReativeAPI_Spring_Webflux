package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxColdHotStream {

    @Test
    // Cold - Each Subscriber will start from First element of input stream.
    public void coldPublisher_Sequential() throws InterruptedException {
        Flux<Integer> stream = Flux.just(1,2,3,4,5)
                .delayElements(Duration.ofSeconds(1))
                .log();

        stream.blockLast();
        // Because of blockLast() the Subscribers will run one after another
        stream.blockLast();

    }

    @Test
    // Cold - Each Subscriber will start from First element of input stream.
    public void coldPublisher_Parallel() throws InterruptedException {
        Flux stream = Flux.just(1,2,3,4,5)
                .delayElements(Duration.ofSeconds(1))
                .log();

        stream.subscribe((v) -> System.out.println("Subscriber A:"+v));
        Thread.sleep(500);
        stream.subscribe((v) -> System.out.println("Subscriber B:"+v));

        Thread.sleep(8000);

    }


    @Test
    // Hot - Each Subscriber will start from current element in stream instead of from beginning
    public void hotPublisher() throws InterruptedException {
        Flux stream = Flux.just(1,2,3,4,5)
                .delayElements(Duration.ofSeconds(1));

        // https://projectreactor.io/docs/core/release/api/reactor/core/publisher/ConnectableFlux.html
        // The abstract base class for connectable publishers that let subscribers pile up before they connect to their data source.
        ConnectableFlux<Integer> connFlux = stream.publish();
        connFlux.connect(); // Connect this ConnectableFlux to its source and return a Disposable that can be used for disconnecting.
        connFlux.subscribe((v) -> System.out.println("Publisher A:"+v));
        Thread.sleep(3000); // wait for 3 seconds so that Publisher will start with 3 second delay
        connFlux.subscribe((v) -> System.out.println("Publisher B:"+v));

        Thread.sleep(8000);
       /* Output :
        Publisher A:1
        Publisher A:2
        Publisher A:3
        Publisher B:3
        Publisher A:4
        Publisher B:4
        Publisher A:5
        Publisher B:5*/
    }
}

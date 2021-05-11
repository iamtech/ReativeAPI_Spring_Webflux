package edu.am.reactor.amreactor.samples;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Duration;

public class FluxBackPressure {

    @Test
    public void backpressure_limitRequest() {
        Flux<Integer> fluxFinite = Flux.range(1, 10)
                .log();

        // Deprecated
        /*fluxFinite.subscribe(val -> System.out.println("value is:"+val)
                , err -> System.err.println("Error is"+err)
                , System.out.println("Completed")
                ,(subscription) -> subscription.request(2));
        */

        // https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#limitRequest-long-
        fluxFinite.limitRequest(7).blockLast();

      /*  Public final Flux<T> take(long n)
        Take only the first N values from this Flux, if available.*/

    }


    @Test
    public void backpressure_BaseSubscriber() {
        Flux<Integer> fluxFinite = Flux.range(1, 10)
                .doOnRequest(r -> System.out.println("request of " + r));
        //     .log();

       /* the hookOnSubscribe method prints a statement to standard out and makes the first request.
        Then the hookOnNext method prints a statement and performs additional requests, one request at a time.*/
        fluxFinite.subscribeWith(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(3);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
                // request(1);
                //requestUnbounded();
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("On Complete");

            }

            @Override
            protected void hookOnError(Throwable throwable) {
                super.hookOnError(throwable);
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("On Cancel");
                super.hookOnCancel();
            }
        });

        /*Output :
        Subscribed
        request of 3
        1
        2
        3
*/
    }


    // Limit number of elements to be subscribed recursively
    @Test
    public void backpressure_Subscriber() {
        Flux<Integer> fluxFinite = Flux.range(1, 10)
                //  .delayElements(Duration.ofSeconds(1))
                .doOnRequest(r -> System.out.println("request of " + r));
        //     .log();

       /* the hookOnSubscribe method prints a statement to standard out and makes the first request.
        Then the hookOnNext method prints a statement and performs additional requests, one request at a time.*/
        fluxFinite.subscribeWith(new Subscriber<Integer>() {
            private Subscription s;
            int onNextAmount;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                System.out.println("Subscribed");
                this.s.request(2);           // Initiated request with 2 elements limit
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("from onNext:" + integer);
                onNextAmount++;
                if (onNextAmount % 2 == 0) {
                    s.request(2);       // Fetch for request with 2 elements
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error Occurred:" + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        });
    }


    @Test
    public void backpressure_BaseSubscriber_Cancel() {
        Flux<Integer> fluxFinite = Flux.range(1, 10)
                //  .delayElements(Duration.ofSeconds(1))
                .doOnRequest(r -> System.out.println("request of " + r));
        //     .log();

       /* the hookOnSubscribe method prints a statement to standard out and makes the first request.
        Then the hookOnNext method prints a statement and performs additional requests, one request at a time.*/
        fluxFinite.subscribeWith(new BaseSubscriber<Integer>() {
            int elementCount;

            @Override
            public void dispose() {
               System.out.println("Dispose Called");
            }

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscription Started");
                subscription.request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
               // System.out.println("Getting OnNext");
                System.out.println(value);
                elementCount++;
                if(elementCount % 2==0){
                    request(2);
                }
                if(elementCount == 6){
                    cancel();
                }
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("hookOnComplete Called");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("hookOnCancel Called");
            }

            @Override
            protected void hookFinally(SignalType type) {
                System.out.println("hookFinally Called");
            }
        });

     /*   Output:
        Subscription Started
        request of 2
        1
        2
        request of 2
        3
        4
        request of 2
        5
        6
        request of 2
        hookOnCancel Called
        hookFinally Called*/
    }
}

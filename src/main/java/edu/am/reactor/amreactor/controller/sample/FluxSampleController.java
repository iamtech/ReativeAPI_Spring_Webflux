package edu.am.reactor.amreactor.controller.sample;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxSampleController {

   @GetMapping(path = "/fluxIntStream",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Integer> getIntStream(){

       return Flux.just(1,2,3,4,5,6,7)
               .delayElements(Duration.ofSeconds(1))
               .log();
   }


    @GetMapping(path = "/fluxIntStreamResp",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Integer>> getIntStreamInResponse(){

        Flux<Integer> intflux = Flux.just(1,2,3,4,5,6,7)
                .delayElements(Duration.ofSeconds(1))
                .log();

/*        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION , "inline");*/

        return ResponseEntity.ok()
                //.headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(intflux);
    }

    @GetMapping(path = "/fluxInfiniteStream",produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Long> getIntStream_infinite(){

        return  Flux.interval(Duration.ofSeconds(1))
                .log();
    }
    // curl -X GET http://localhost:8080/fluxInfiniteStream
}

package ru.bms.webservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.bpsapi.HelloResponse;

@RestController
public class BPSController {
    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello, my friend!").build());
    }
}

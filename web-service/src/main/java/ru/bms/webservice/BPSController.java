package ru.bms.webservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.api.HelloResponse;
import ru.bms.webservice.api.PutPaymentRequest;
import ru.bms.webservice.api.PutPaymentResponse;
import ru.bms.webservice.service.PaymentMapper;
import ru.bms.webservice.service.PaymentService;

@RestController
@Log
public class BPSController {

    @Autowired
    PaymentMapper paymentMapper;
    @Autowired
    PaymentService paymentService;

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello, my friend! I`m BPS Controller.").build());
    }

    @PostMapping("/payment")
    public Mono<PutPaymentResponse> payment(@RequestBody PutPaymentRequest request) {
        log.info("post /payment ");
        log.info(request.toString());
        return paymentService.payment(paymentMapper.mapRequest(request))
                .map(r -> paymentMapper.mapResponse(r));
    }

}

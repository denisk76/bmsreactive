package ru.bms.paymentapi.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;
import ru.bms.paymentapi.service.PaymentService;

@Log
public class PilotPaymentService implements PaymentService {

    @Autowired
    @Qualifier("paymentWebClient")
    WebClient webClient;

    @Override
    public Mono<PaymentResponse> payment(PaymentRequest request) {
        log.info("Pilot payment service run payment ...");
        log.info(request.toString());
        Mono<PaymentResponse> paymentResponseMono = webClient
                .post().uri("/getPayment").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(PaymentResponse.class);
        return paymentResponseMono;
    }
}

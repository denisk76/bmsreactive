package ru.bms.handlerservice.service.pilot;

import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.handlerservice.service.PaymentService;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.PaymentResponse;

@Log
public class PilotPaymentService implements PaymentService {

    WebClient webClient;

    public PilotPaymentService() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8082").build();
    }

    @Override
    public Mono<PaymentResponse> payment(PaymentRequest request) {
        log.info("Pilot payment service run payment ...");
        log.info(request.toString());
        return webClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(PaymentResponse.class);
    }
}

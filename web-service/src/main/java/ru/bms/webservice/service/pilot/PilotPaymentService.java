package ru.bms.webservice.service.pilot;

import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.webservice.service.PaymentService;

@Log
public class PilotPaymentService implements PaymentService {

    WebClient webClient;

    public PilotPaymentService() {
        this.webClient = WebClient.builder().baseUrl("http://localhost:8081").build();
    }

    @Override
    public Mono<BPSPaymentResponse> payment(BPSPaymentOperation request) {
        log.info("Pilot payment service run payment ...");
        log.info(request.toString());
        return webClient.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(BPSPaymentResponse.class);
    }
}

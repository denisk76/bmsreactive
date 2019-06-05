package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;
import ru.bms.service.ClientService;

@Log
public class PilotClientService implements ClientService {

    @Autowired
    @Qualifier("clientWebClient")
    WebClient webClient;

    @Override
    public Mono<ClientResponse> getClient(ClientRequest request) {
        log.info("Pilot Client Service run getClient ...");
        log.info(request.toString());
        return webClient.post().uri("/getClient").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(ClientResponse.class);
    }
}

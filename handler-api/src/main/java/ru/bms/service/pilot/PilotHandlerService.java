package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.UpdateConfigRequest;
import ru.bms.service.HandlerService;

@Log
public class PilotHandlerService implements HandlerService {

    @Autowired
    @Qualifier("handlerWebClient")
    WebClient webClient;

    @Override
    public void setIpAddr(String ipAddr) {
        log.info("set ip addr for handler service: " + ipAddr);
        this.webClient = webClient.mutate().baseUrl(ipAddr).build();
    }

    @Override
    public Mono<String> updateConfig(UpdateConfigRequest request) {
        log.info("Pilot Handler Service run updateConfig ...");
        log.info(request.toString());
        return webClient.post().uri("/updateConfig").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(String.class);
    }

}

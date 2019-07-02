package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.UpdateConfigRequest;
import ru.bms.exceptions.HandlerApiException;
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
    public String updateConfig(UpdateConfigRequest request) {
        log.info("Pilot Handler Service run updateConfig ...");
        log.info(request.toString());
        try {
            return webClient.post().uri("/config").accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromObject(request))
                    .retrieve().onStatus(HttpStatus::isError, a -> {
                        System.out.println("Status code: " + a.statusCode());
                        return Mono.error(new HandlerApiException());
                    })
                    .bodyToMono(String.class).block();
        } catch (Exception e) {
            log.info(e.getClass().getName());
            log.info(e.getMessage());
            throw e;
        }
    }

}

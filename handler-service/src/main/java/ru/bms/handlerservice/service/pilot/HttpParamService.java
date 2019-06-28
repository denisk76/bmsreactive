package ru.bms.handlerservice.service.pilot;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.handlerservice.service.ParamService;

public class HttpParamService implements ParamService {
    @Override
    public Mono<String> getParam(String url, String method, String json) {
        WebClient webClient = WebClient.builder().baseUrl(url).build();
        return webClient.post().uri(method).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(json))
                .retrieve().bodyToMono(String.class);
    }
}

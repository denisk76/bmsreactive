package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.AddTerminalRequest;
import ru.bms.AddTerminalResponse;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.exceptions.TerminalApiException;
import ru.bms.service.TerminalService;

@Log
public class PilotTerminalService implements TerminalService {

    @Autowired
    @Qualifier("terminalWebClient")
    WebClient webClient;

    @Override
    public void setIpAddr(String ipAddr) {
        log.info("set ip addr for terminal service: " + ipAddr);
        this.webClient = webClient.mutate().baseUrl(ipAddr).build();
    }

    @Override
    public Mono<TerminalResponse> getTerminal(TerminalRequest request) {
        log.info("Pilot terminal service run getTerminal ...");
        log.info(request.toString());
        Mono<TerminalResponse> terminalResponseMono = webClient.post().uri("/getTerminal").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(TerminalResponse.class);
        return terminalResponseMono;
    }

    @Override
    public void addTerminals(String request) {
        log.info("Pilot terminal service run addTerminals ...");
        log.info(request);
        JSONArray jsonArray = new JSONArray(request);
        for (int i = 0; i < jsonArray.length(); i++) {
            String json = jsonArray.get(i).toString();
            System.out.println("Send json: " + json);
            try {
                webClient.post().uri("/addTerminal").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(json))
                        .retrieve().onStatus(HttpStatus::isError, a -> {
                    System.out.println("Status code: " + a.statusCode());
                    return Mono.error(new TerminalApiException());
                })
                        .bodyToMono(AddTerminalResponse.class).block();
            } catch (Exception e) {
                log.info(e.getClass().getName());
                log.info(e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public Mono<AddTerminalResponse> addTerminal(AddTerminalRequest request) {
        log.info("Pilot terminal service run addTerminal ...");
        log.info(request.toString());
        return webClient.post().uri("/addTerminal").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(AddTerminalResponse.class);
    }


}

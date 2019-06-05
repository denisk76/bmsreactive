package ru.bms.service.pilot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.service.TerminalService;

@Log
public class PilotTerminalService implements TerminalService {

    @Autowired
    @Qualifier("terminalWebClient")
    WebClient webClient;

    @Override
    public Mono<TerminalResponse> getTerminal(TerminalRequest request) {
        log.info("Pilot terminal service run getTerminal ...");
        log.info(request.toString());
        return webClient.post().uri("/getTerminal").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .retrieve().bodyToMono(TerminalResponse.class);
    }
}

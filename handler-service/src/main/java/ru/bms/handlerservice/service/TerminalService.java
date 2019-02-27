package ru.bms.handlerservice.service;

import reactor.core.publisher.Mono;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;

public interface TerminalService {
    Mono<TerminalResponse> getTerminal(TerminalRequest request);
}

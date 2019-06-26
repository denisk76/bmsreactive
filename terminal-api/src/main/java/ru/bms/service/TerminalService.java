package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.AddTerminalRequest;
import ru.bms.AddTerminalResponse;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;

public interface TerminalService {
    void setIpAddr(String ipAddr);

    Mono<TerminalResponse> getTerminal(TerminalRequest request);

    Mono<AddTerminalResponse> addTerminal(AddTerminalRequest request);

    void addTerminals(String request);
}

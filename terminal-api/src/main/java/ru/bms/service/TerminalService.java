package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.AddTerminalRequest;
import ru.bms.AddTerminalResponse;
import ru.bms.api.ITerminal;
import ru.bms.api.RuleUnit;

public interface TerminalService {
    void setIpAddr(String ipAddr);

    Mono<RuleUnit> getTerminal(ITerminal terminal);

    Mono<AddTerminalResponse> addTerminal(AddTerminalRequest request);

    void addTerminals(String request);
}

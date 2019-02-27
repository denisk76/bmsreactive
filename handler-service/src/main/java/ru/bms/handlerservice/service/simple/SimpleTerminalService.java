package ru.bms.handlerservice.service.simple;

import reactor.core.publisher.Mono;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.api.RuleUnit;
import ru.bms.handlerservice.service.TerminalService;

import java.math.BigDecimal;

public class SimpleTerminalService implements TerminalService {
    @Override
    public Mono<TerminalResponse> getTerminal(TerminalRequest request) {
        return Mono.just(TerminalResponse.builder().ruleUnit(RuleUnit.builder().percent(BigDecimal.valueOf(20)).build()).build());
    }
}

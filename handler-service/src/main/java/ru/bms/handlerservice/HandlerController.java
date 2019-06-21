package ru.bms.handlerservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.ClientRequest;
import ru.bms.ClientResponse;
import ru.bms.TerminalRequest;
import ru.bms.TerminalResponse;
import ru.bms.api.HelloResponse;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.service.PaymentService;
import ru.bms.service.ClientService;
import ru.bms.service.TerminalService;

@RestController
@Log
public class HandlerController {

    @Autowired
    PaymentService paymentService;
    @Autowired
    ClientService clientService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    PaymentMapper mapper;

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello!").build());
    }


    @PostMapping("/payment")
    public Mono<BPSPaymentResponse> payment(@RequestBody BPSPaymentOperation operation) {
        log.info("post /payment");
        log.info(operation.toString());

        return
                Mono.just(operation)
                        .map(mapper::mapRequest)
                        .zipWith(clientService.getClient(ClientRequest.builder()
                                        .client(operation.getClient())
                                        .build()),
                                this::addClient)
                        .zipWith(terminalService.getTerminal(TerminalRequest.builder()
                                        .terminal(operation.getTerminal())
                                        .build()),
                                this::addTerminal)
                        .flatMap(paymentService::payment)
                        .map(mapper::mapResponse);
    }

    private PaymentRequest addClient(PaymentRequest request, ClientResponse clientResponse) {
        request.add(PaymentRequest.ParamType.ACCOUNT, clientResponse.getAccount());
        return request;
    }

    private PaymentRequest addTerminal(PaymentRequest request, TerminalResponse terminalResponse) {
        request.add(PaymentRequest.ParamType.RULE_UNIT, terminalResponse.getRuleUnit());
        return request;
    }
}
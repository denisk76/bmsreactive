package ru.bms.handlerservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.api.Account;
import ru.bms.api.HelloResponse;
import ru.bms.api.RuleUnit;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.handlerservice.service.ParamService;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.paymentapi.PaymentRequest;
import ru.bms.paymentapi.service.PaymentService;
import ru.bms.service.ClientService;
import ru.bms.service.TerminalService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    ParamService paramService;

    private Map<BPSPaymentOperation.ParamType, ConfigLine> map;

    public HandlerController() {
        map = new HashMap<>();
        map.put(BPSPaymentOperation.ParamType.CLIENT, ConfigLine.builder()
                .paramType(PaymentRequest.ParamType.ACCOUNT)
//                .url("http://client-service:8080")
                .url("http://localhost:8001")
                .method("/getClient")
                .build());
        map.put(BPSPaymentOperation.ParamType.TERMINAL, ConfigLine.builder()
                .paramType(PaymentRequest.ParamType.RULE_UNIT)
//                .url("/http://terminal-service:8080")
                .url("http://localhost:8000")
                .method("/getTerminal")
                .build());
    }

    private String getUrl(BPSPaymentOperation.ParamType type) {
        ConfigLine line = map.get(type);
        return line.getUrl();
    }

    private String getMethod(BPSPaymentOperation.ParamType type) {
        ConfigLine line = map.get(type);
        return line.getMethod();
    }

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
                        .zipWith(paramService.getParam(
                                getUrl(BPSPaymentOperation.ParamType.CLIENT),
                                getMethod(BPSPaymentOperation.ParamType.CLIENT),
                                operation.get(BPSPaymentOperation.ParamType.CLIENT)
                                ),
                                this::addClient)
                        .zipWith(paramService.getParam(
                                getUrl(BPSPaymentOperation.ParamType.TERMINAL),
                                getMethod(BPSPaymentOperation.ParamType.TERMINAL),
                                operation.get(BPSPaymentOperation.ParamType.TERMINAL)
                                ),
                                this::addTerminal)
//                        .zipWith(clientService.getClient(ClientRequest.builder()
//                                        .client(operation.getClient())
//                                        .build()),
//                                this::addClient)
//                        .zipWith(terminalService.getTerminal(TerminalRequest.builder()
//                                        .terminal(operation.getTerminal())
//                                        .build()),
//                                this::addTerminal)
                        .flatMap(paymentService::payment)
                        .map(mapper::mapResponse);
    }

//    private PaymentRequest addClient(PaymentRequest request, ClientResponse clientResponse) {
//        request.add(PaymentRequest.ParamType.ACCOUNT, clientResponse.getAccount());
//        return request;
//    }

    private PaymentRequest addClient(PaymentRequest request, String clientResponse) {
        try {
            request.add(PaymentRequest.ParamType.ACCOUNT, Account.fromJson(clientResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }

    //    private PaymentRequest addTerminal(PaymentRequest request, TerminalResponse terminalResponse) {
//        request.add(PaymentRequest.ParamType.RULE_UNIT, terminalResponse.getRuleUnit());
//        return request;
//    }
    private PaymentRequest addTerminal(PaymentRequest request, String terminalResponse) {
        try {
            request.add(PaymentRequest.ParamType.RULE_UNIT, RuleUnit.fromJson(terminalResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }
}
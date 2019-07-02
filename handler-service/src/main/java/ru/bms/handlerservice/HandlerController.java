package ru.bms.handlerservice;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.bms.UpdateConfigRequest;
import ru.bms.api.ApiParamType;
import ru.bms.api.HelloResponse;
import ru.bms.bpsapi.BPSPaymentOperation;
import ru.bms.bpsapi.BPSPaymentResponse;
import ru.bms.bpsapi.ConfigLine;
import ru.bms.bpsapi.InputParamType;
import ru.bms.handlerservice.service.ParamService;
import ru.bms.handlerservice.service.PaymentMapper;
import ru.bms.handlerservice.service.WebConfiguration;
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
    @Autowired
    ParamService paramService;
    @Autowired
    WebConfiguration webConfiguration;

    public HandlerController() {
    }

    private String getUrl(InputParamType type) {
        ConfigLine line = webConfiguration.get(type);
        log.info("get url for " + type + ": " + line.getUrl());
        return line.getUrl();
    }

    private String getMethod(InputParamType type) {
        ConfigLine line = webConfiguration.get(type);
        log.info("get method for " + type + ": " + line.getMethod());
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
                                getUrl(InputParamType.CLIENT),
                                getMethod(InputParamType.CLIENT),
                                operation.get(InputParamType.CLIENT)
                                ),
                                this::addClient)
                        .zipWith(paramService.getParam(
                                getUrl(InputParamType.TERMINAL),
                                getMethod(InputParamType.TERMINAL),
                                operation.get(InputParamType.TERMINAL)
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

    @PostMapping("/config")
    public String updateConfig(@RequestBody UpdateConfigRequest request) {
        webConfiguration.update(request.getInputParamType(), request.getUrl());
        return "SUCCESS";
    }

//    private PaymentRequest addClient(PaymentRequest request, ClientResponse clientResponse) {
//        request.add(PaymentRequest.ParamType.ACCOUNT, clientResponse.getAccount());
//        return request;
//    }

    private PaymentRequest addClient(PaymentRequest request, String clientResponse) {
            request.add(ApiParamType.ACCOUNT, clientResponse);
        return request;
    }

    //    private PaymentRequest addTerminal(PaymentRequest request, TerminalResponse terminalResponse) {
//        request.add(PaymentRequest.ParamType.RULE_UNIT, terminalResponse.getRuleUnit());
//        return request;
//    }
    private PaymentRequest addTerminal(PaymentRequest request, String terminalResponse) {
            request.add(ApiParamType.RULE_UNIT, terminalResponse);
        return request;
    }
}
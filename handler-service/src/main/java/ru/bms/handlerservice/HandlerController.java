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

    private ApiParamType getApiType(InputParamType type) {
        ConfigLine line = webConfiguration.get(type);
        log.info("get api type for " + type + ": " + line.getMethod());
        return line.getParamType();
    }

    @GetMapping("/hello")
    public Mono<HelloResponse> hello() {
        return Mono.just(HelloResponse.builder().message("Hello!").build());
    }


    @PostMapping("/payment")
    public Mono<BPSPaymentResponse> payment(@RequestBody BPSPaymentOperation operation) {
        log.info("post /payment");
        log.info(operation.toString());
        Mono<PaymentRequest> mono = Mono.just(operation)
                .map(mapper::mapRequest);
        for (String key : operation.params.keySet()) {
            if (key.equals(InputParamType.OPERATION.name())) continue;
            InputParamType type = InputParamType.valueOf(key);
            mono = zipParam(mono, operation, type, getApiType(type));
        }
        return mono
                .flatMap(paymentService::payment)
                .map(mapper::mapResponse);
    }

    private PaymentRequest addParam(PaymentRequest request, String value, ApiParamType type) {
        request.add(type, value);
        return request;
    }

    private Mono<PaymentRequest> zipParam(Mono<PaymentRequest> mono, BPSPaymentOperation operation, InputParamType type, ApiParamType apiType) {
        return mono.zipWith(paramService.getParam(
                getUrl(type),
                getMethod(type),
                operation.get(type)
                ),
                (p, s) -> this.addParam(p, s, apiType));
    }

    @PostMapping("/config")
    public String updateConfig(@RequestBody UpdateConfigRequest request) {
        webConfiguration.update(request.getInputParamType(), request.getUrl());
        return "SUCCESS";
    }
}
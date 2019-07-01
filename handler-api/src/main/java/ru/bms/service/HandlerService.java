package ru.bms.service;

import reactor.core.publisher.Mono;
import ru.bms.UpdateConfigRequest;

public interface HandlerService {
    void setIpAddr(String ipAddr);

Mono<String> updateConfig(UpdateConfigRequest request);

}

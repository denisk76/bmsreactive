package ru.bms.handlerservice.service;

import reactor.core.publisher.Mono;

public interface ParamService {
    Mono<String> getParam(String url, String method, String json);
}

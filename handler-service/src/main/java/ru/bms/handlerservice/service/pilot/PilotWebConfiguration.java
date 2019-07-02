package ru.bms.handlerservice.service.pilot;

import lombok.extern.java.Log;
import ru.bms.api.ApiParamType;
import ru.bms.bpsapi.ConfigLine;
import ru.bms.bpsapi.InputParamType;
import ru.bms.handlerservice.service.WebConfiguration;

import java.util.HashMap;
import java.util.Map;

@Log
public class PilotWebConfiguration implements WebConfiguration {

    private Map<InputParamType, ConfigLine> map;

    public PilotWebConfiguration() {
        map = new HashMap<>();
        map.put(InputParamType.CLIENT, ConfigLine.builder()
                .paramType(ApiParamType.ACCOUNT)
//                .url("http://client-service:8080")
                .url("http://localhost:8001")
                .method("/getClient")
                .build());
        map.put(InputParamType.TERMINAL, ConfigLine.builder()
                .paramType(ApiParamType.RULE_UNIT)
//                .url("/http://terminal-service:8080")
                .url("http://localhost:8000")
                .method("/getTerminal")
                .build());
    }


    @Override
    public void add(InputParamType type, ConfigLine configLine) {
        map.put(type, configLine);
    }

    @Override
    public void update(String type, String url) {
        log.info("set module url: " + type + " = " + url);
        ConfigLine configLine = map.get(InputParamType.valueOf(type));
        log.info("old url = " + configLine.getUrl());
        configLine.setUrl(url);
//        map.replace(type, configLine);
    }

    @Override
    public ConfigLine get(InputParamType type) {
        return map.get(type);
    }
}

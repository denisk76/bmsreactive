package ru.bms.handlerservice.service;

import ru.bms.bpsapi.ConfigLine;
import ru.bms.bpsapi.InputParamType;

public interface WebConfiguration {
    void add(InputParamType type, ConfigLine configLine);

    void update(String type, String url);

    ConfigLine get(InputParamType type);
}

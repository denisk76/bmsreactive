package ru.bms.service;

import ru.bms.UpdateConfigRequest;

public interface HandlerService {
    void setIpAddr(String ipAddr);

    String updateConfig(UpdateConfigRequest request);

}

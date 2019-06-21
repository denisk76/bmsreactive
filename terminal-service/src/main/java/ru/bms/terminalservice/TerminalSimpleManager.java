package ru.bms.terminalservice;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Log
public class TerminalSimpleManager implements TerminalManager {

    private Map<String, BigDecimal> map;

    public TerminalSimpleManager() {
        map = new HashMap<>();
    }

    @Override
    public BigDecimal getByCode(String terminalCode) {
//        switch (code) {
//            case "10": percent = BigDecimal.TEN; break;
//            case "20": percent = BigDecimal.valueOf(20); break;
//            default: percent = BigDecimal.ONE; break;
//        }
        return map.get(terminalCode);
    }

    @Override
    public void add(String code, BigDecimal percent) {
        log.info("put terminal with code = " + code);
        map.put(code, percent);
    }
}

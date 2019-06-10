package ru.bms.terminalservice;

import java.math.BigDecimal;

public interface TerminalManager {
    BigDecimal getByCode(String terminalCode);

    void add(String code, BigDecimal percent);
}

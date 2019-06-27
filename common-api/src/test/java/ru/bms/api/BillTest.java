package ru.bms.api;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BillTest {

    @Test
    void toString1() {
        Bill bill = Bill.builder().sum(BigDecimal.TEN).build();
        System.out.println(bill.toString());
    }
}
package ru.bms.webservice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;

public class BaseTest {
    private static ClientAndServer handlerMockServer;

    @BeforeAll
    public static void setupMockServer() {
        handlerMockServer = ClientAndServer.startClientAndServer(8000);
        Expectations.createTerminalExpectations(handlerMockServer);
    }

    @AfterAll
    public static void after() {
        handlerMockServer.stop();
    }
}

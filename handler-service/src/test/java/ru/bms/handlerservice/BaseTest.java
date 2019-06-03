package ru.bms.handlerservice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockserver.integration.ClientAndServer;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class BaseTest {
    private static ClientAndServer terminalMockServer;
    private static ClientAndServer clientMockServer;
    private static ClientAndServer paymentMockServer;

    @BeforeAll
    public static void setupMockServer() throws IOException, JAXBException {
        terminalMockServer = ClientAndServer.startClientAndServer(8000);
        clientMockServer = ClientAndServer.startClientAndServer(8001);
        paymentMockServer = ClientAndServer.startClientAndServer(8002);
        Expectations.createTerminalExpectations(terminalMockServer);
        Expectations.createClientExpectations(clientMockServer);
        Expectations.createPaymentExpectations(paymentMockServer);
    }

    @AfterAll
    public static void after() {
        terminalMockServer.stop();
        clientMockServer.stop();
        paymentMockServer.stop();
    }
}

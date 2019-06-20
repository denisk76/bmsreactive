package ru.bms

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.reactive.function.BodyInserters
import ru.bms.api.HelloResponse
import ru.bms.webservice.BPSTestConfig
import ru.bms.webservice.BaseTest
import ru.bms.webservice.WebServiceApplication
import ru.bms.webservice.api.PutPaymentResponse

@ExtendWith(SpringExtension::class)
@WebFluxTest
@ContextConfiguration(classes = [WebServiceApplication::class, BPSTestConfig::class])
class WebServiceAppTest :BaseTest() {

    @Autowired
    lateinit var web: WebTestClient


    @Test
    fun simpleTest() {
        println("simple test start ...")
        assertNotNull(web, "webTestClient is NULL !!!")
        println("simple test end.")
    }

    @Test
    @Throws(Exception::class)
    fun testPayment() {
        val payment = """
            {
             "terminalCode":"345t",
             "cardNum":"1234",
             "bill": {
              "sum":10
             }
            }
        """.trimIndent()

        val response = """
            {
            "spend":4,
            "earn":3,
            "amount":10
            }
        """.trimIndent()
        web.post().uri("/payment").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(payment))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(response)
                .jsonPath("$.earn").isEqualTo(3)
                .jsonPath("$.spend").isEqualTo(4)
                .jsonPath("$.amount").isEqualTo(10)
    }

    @Test
    @Throws(Exception::class)
    fun testHello() {
        web.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.message").isEqualTo("Hello, my friend! I`m BPS Controller.")
    }
}
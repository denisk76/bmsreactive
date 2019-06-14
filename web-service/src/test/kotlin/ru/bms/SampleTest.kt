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
import ru.bms.api.HelloResponse
import ru.bms.webservice.BPSTestConfig
import ru.bms.webservice.WebServiceApplication

@ExtendWith(SpringExtension::class)
@WebFluxTest
@ContextConfiguration(classes = [WebServiceApplication::class, BPSTestConfig::class])
class SampleTest {

    @Autowired
    private val webTestClient: WebTestClient? = null


    @Test
    fun simpleTest() {
        println("simple test start ...")
        assertNotNull(webTestClient,"webTestClient is NULL !!!")
        println("simple test end.")
    }

    @Test
    @Throws(Exception::class)
    fun testHello() {
        HelloResponse.builder().message("").build()
        if (webTestClient != null) {
//            val expectBody: WebTestClient.BodySpec<HelloResponse, *> =
            val returnResult = webTestClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk
                    .expectBody(HelloResponse::class.java)
                    .returnResult()
            println(returnResult.responseBody?.message)
            println("1234")
            val jsonString = """
                {
                  "type":"payment",
                  "amount":"112"
                  "bill":[
                  {
                    "id":1,
                    "good":"bonbon"
                  },
                  {
                    "id":2,
                    "good":"cookie"
                  }
                  ]
                }
            """.trimIndent()
            println(jsonString)
//            expectBody.isEqualTo<Nothing>(HelloResponse.builder().message("Hello, my friend! I`m BPS Controller.").build())
//                    .isEqualTo(HelloResponse.builder().message("Hello, my friend! I`m BPS Controller.").build())
        }
    }
}
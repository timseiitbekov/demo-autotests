package com.tests.implementation.gatlingsetups

import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.core.CoreDsl.constantUsersPerSec
import io.gatling.javaapi.core.CoreDsl.incrementUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status

class SimulationExample3 : Simulation() {

    private val httpProtocol = http
        .baseUrl("https://e54c0169-f69f-471c-8b44-26825854cc4d.mock.pstmn.io")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val scn = scenario("POST demo endpoint")
        .exec(
            http("Second demo gatling requests")
                .post("/demo-endpoint")
                .body(StringBody("""{"num": "12345"}""")).asJson()
                .check(
                    status().shouldBe(201)
                )
        )

    private val scn2 = scenario("GET demo endpoint")
        .exec(
            http("GET demo gatling request")
                .get("/demo-endpoint")
                .check(
                    status().shouldBe(200)
                )
        )

    init {
        setUp(
            scn.injectOpen(
                incrementUsersPerSec(10.0).times(5).eachLevelLasting(5)
            ),
            scn2.injectOpen(
                constantUsersPerSec(100.0).during(10)
            )
        ).protocols(httpProtocol)
    }
}
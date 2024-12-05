package com.tests.implementation.gatlingsetups

import io.gatling.javaapi.core.CoreDsl.rampUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.time.Duration

class SimulationExample : Simulation() {

    private val httpProtocol = http
        .baseUrl("https://ba09d4d6-46cb-4b62-9a20-b6e3c35fa9ae.mock.pstmn.io")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val scn = scenario("demogatling")
        .exec(
            http("some demo gatling get request")
                .get("/demogatling")
                .check(
                    status().shouldBe(200)
                )
        )

    init {
        setUp(
            scn.injectOpen(
                rampUsersPerSec(200.0)
                    .to(500.0)
                    .during(Duration.ofSeconds(20))
            )
        ).protocols(httpProtocol)
    }
}

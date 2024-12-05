package com.tests.implementation.gatlingsetups

import io.gatling.javaapi.core.CoreDsl.rampUsersPerSec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.core.CoreDsl.stressPeakUsers
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import java.time.Duration

class SimulationExample2 : Simulation() {

    private val httpProtocol = http
        .baseUrl("https://099f3ce2-d915-4429-a1bd-7ec0ca90d8e4.mock.pstmn.io")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")

    private val scn = scenario("Second demo gatling requests")
        .exec(
            http("Second demo gatling requests")
                .get("/random")
                .check(
                    status().shouldBe(200)
                )
        )

    init {
        setUp(
            scn.injectOpen(
                rampUsersPerSec(2.0)
                    .to(10.0)
                    .during(Duration.ofSeconds(20))
            ),
            scn.injectOpen(
                stressPeakUsers(10000)
                    .during(Duration.ofSeconds(10))
            )
        ).protocols(httpProtocol)
    }
}

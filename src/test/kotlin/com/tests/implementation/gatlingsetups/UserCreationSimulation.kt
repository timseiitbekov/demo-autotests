package com.tests.implementation.gatlingsetups

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.http.*
import io.github.serpro69.kfaker.Faker
import java.time.Duration

class UserCreationSimulation : Simulation() {

    private val faker = Faker()

    private val httpProtocol = HttpDsl.http
        .baseUrl("http://localhost:8080")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")

    private fun generateUser(): String {
        val firstName = faker.name.firstName()
        val lastName = faker.name.lastName()
        val email = faker.internet.email()
        val password = faker.internet.domain()

        return """
            {
                "first_name": "$firstName",
                "last_name": "$lastName",
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
    }

    private val scn = CoreDsl.scenario("User Creation Load Test")
        .exec(
            HttpDsl.http("Create User")
                .post("/users")
                .body(StringBody { generateUser() })
                .check(
                    HttpDsl.status().`is`(201)
                )
        )

    init {
        setUp(
            listOf(
                scn.injectOpen(
                    CoreDsl.rampUsersPerSec(20.0)
                        .to(100.0)
                        .during(Duration.ofSeconds(10))
                )
            )
        ).protocols(httpProtocol)
    }
}
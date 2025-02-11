package com.tests.implementation.gatlingsetups

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.StringBody
import io.gatling.javaapi.http.*
import io.github.serpro69.kfaker.Faker
import java.time.Duration
import kotlin.random.Random

class BookCreationSimulation : Simulation() {

    private val faker = Faker()

    private val httpProtocol = HttpDsl.http
        .baseUrl("http://localhost:8080")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")

    private fun generateBook(): String {
        val title = faker.book.title()
        val author = faker.book.author()
        val genre = faker.book.genre()
        val isbn = faker.phoneNumber.phoneNumber()
        val price = Random.nextDouble()
        val publishYear = Random.nextInt(1800, 2025)

        return """
            {
                "title": "$title",
                "author": "$author",
                "genre": "$genre",
                "isbn": "ISBN$isbn",
                "price": $price,
                "publish_year": $publishYear
            }
        """.trimIndent()
    }

    private val scn = CoreDsl.scenario("Book Creation Load Test")
        .exec(
            HttpDsl.http("Create Book")
                .post("/books")
                .body(StringBody { generateBook() })
                .check(
                    HttpDsl.status().`is`(201)
                )
        )

    init {
        setUp(
            scn.injectOpen(
                listOf(
                    CoreDsl.rampUsers(2000).during(Duration.ofSeconds(5))
                )
            )
        ).protocols(httpProtocol)
    }
}
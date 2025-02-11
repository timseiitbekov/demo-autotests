package com.tests.implementation.gatlingsetups

import io.gatling.app.Gatling
import org.junit.jupiter.api.Test
import scala.collection.JavaConverters.mapAsScalaMapConverter

class Rand {

    @Test
    fun runLoadMiniLib() {
        val configs: Map<String, String> = mapOf(
            "gatling.core.directory.results" to "gatling-report",
            "gatling.core.directory.binaries" to "build/test-classes",
            "gatling.core.simulationClass" to com.tests.implementation.gatlingsetups.UserCreationSimulation::class.java.name,
        )

        val scalaConfig = mapAsScalaMapConverter(configs).asScala()

        Gatling.fromMap(scalaConfig)
    }

    @Test
    fun runLoadMiniLib2() {
        val configs: Map<String, String> = mapOf(
            "gatling.core.directory.results" to "gatling-report",
            "gatling.core.directory.binaries" to "build/test-classes",
            "gatling.core.simulationClass" to com.tests.implementation.gatlingsetups.BookCreationSimulation::class.java.name,
        )

        val scalaConfig = mapAsScalaMapConverter(configs).asScala()

        Gatling.fromMap(scalaConfig)
    }
}
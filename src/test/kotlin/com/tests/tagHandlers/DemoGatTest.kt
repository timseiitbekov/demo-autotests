package com.tests.tagHandlers

import com.tests.implementation.gatlingsetups.SimulationExample
import com.tests.implementation.gatlingsetups.SimulationExample3
import io.gatling.app.Gatling
import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Owner
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.NORMAL
import io.qameta.allure.Story
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import scala.collection.JavaConverters.mapAsScalaMapConverter

class DemoGatTest {

    @Test
    @Tags(
        Tag("load"),
        Tag("smoke"),
        Tag("blocked")
    )
    @Epic("Load")
    @Feature("Core system")
    @Story("HZ")
    @DisplayName("Load test 1")
    @Description("Some demo description for the load test")
    @Severity(
        NORMAL
    )
    @Owner("Kung-Fu Panda")
    fun runSimulation() {
        val config: Map<String, String> = mapOf(
            "gatling.core.directory.results" to "gatling-report",
            "gatling.core.directory.binaries" to "build/test-classes",
            "gatling.core.simulationClass" to SimulationExample::class.java.name
        )
        // Convert Kotlin Map to Scala mutable Map
        val scalaConfig = mapAsScalaMapConverter(config).asScala()

        Gatling.fromMap(scalaConfig)
    }

    @Test
    @Tags(
        Tag("load"),
        Tag("smoke"),
        Tag("regress")
    )
    fun runSimulation2() {
        val config: Map<String, String> = mapOf(
            "gatling.core.directory.results" to "gatling-report",
            "gatling.core.directory.binaries" to "build/test-classes",
            "gatling.core.simulationClass" to "com.tests.implementation.gatlingsetups.SimulationExample2"
        )

        // Convert Kotlin Map to Scala mutable Map
        val scalaConfig = mapAsScalaMapConverter(config).asScala()

        Gatling.fromMap(scalaConfig)
    }

    @Test
    @Tags(
        Tag("load"),
        Tag("smoke"),
        Tag("regress")
    )
    fun runSimulation3() {
        val config: Map<String, String> = mapOf(
            "gatling.core.directory.results" to "gatling-report",
            "gatling.core.directory.binaries" to "build/test-classes",
            "gatling.core.simulationClass" to SimulationExample3::class.java.name
        )

        // Convert Kotlin Map to Scala mutable Map
        val scalaConfig = mapAsScalaMapConverter(config).asScala()

        Gatling.fromMap(scalaConfig)
    }
}

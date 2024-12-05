plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    java
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

group = "com.tests"
version = "0.0.1-SNAPSHOT"

val allureVersion = "2.24.0"
val cucumberVersion = "7.13.0"
val aspectJVersion = "1.9.20"
val gatlingVersion = "3.10.5"
val fuelVersion = "2.3.1"

tasks.withType(JavaCompile::class) {
    sourceCompatibility = "${JavaVersion.VERSION_17}"
    targetCompatibility = "${JavaVersion.VERSION_17}"
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

configurations {
    create("agent")
}

sourceSets {
    test {
        resources {
            srcDir("src/test/kotlin")
        }
    }
}

tasks {
    test {
        description = "Runs tests with specified tags"
        // https://www.cleantestautomation.com/lessons/filtering-tests-with-maven-and-gradle/#:~:text=Filtering%20Tests%20With%20Gradle

        project.findProperty("tags")?.toString()?.let { tags ->
            useJUnitPlatform {
                includeTags(tags)
            }
        } ?: useJUnitPlatform()

        systemProperties(project.gradle.startParameter.systemPropertiesArgs)
    }
}

tasks.register<Test>("smokeTest") {
    group = "verification"
    description = "Creates separate job to run both cucumber and junit smoke tests"
    systemProperties(project.gradle.startParameter.systemPropertiesArgs)
    useJUnitPlatform {
        includeTags("smoke")
    }
}

tasks.register<Test>("regressTest") {
    group = "verification"
    description = "Creates separate job to run both cucumber and junit regression tests"
    systemProperties(project.gradle.startParameter.systemPropertiesArgs)
    useJUnitPlatform {
        includeTags("regress")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // AspectJ
    implementation("org.aspectj:aspectjrt:$aspectJVersion")
    "agent"("org.aspectj:aspectjweaver:$aspectJVersion")

    // Cucumber
    implementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))
    implementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    implementation("io.cucumber:cucumber-java:$cucumberVersion")
    implementation("io.cucumber:cucumber-spring:$cucumberVersion")

    // Allure
    implementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
    implementation("io.qameta.allure:allure-cucumber7-jvm:$allureVersion")
    implementation("io.qameta.allure:allure-junit-platform:$allureVersion")

    // JUnit
    implementation(platform("org.junit:junit-bom:5.10.0"))
    implementation("org.junit.platform:junit-platform-suite")
    implementation("org.junit.jupiter:junit-jupiter-api")
    implementation("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.junit.platform:junit-platform-suite:1.9.2")

    // Fuel
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    runtimeOnly("com.github.kittinunf.fuel:fuel-gson:$fuelVersion")
    runtimeOnly("com.github.kittinunf.fuel:fuel-coroutines:$fuelVersion")
    implementation("com.github.kittinunf.result:result:4.0.0")

    // Json validation tools
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("io.rest-assured:json-schema-validator")
    implementation("net.javacrumbs.json-unit:json-unit:3.2.2")
    implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")

    // De/Serialization tools
    implementation("com.google.code.gson:gson:2.10.1")

    // Awaitility
    implementation("org.awaitility:awaitility-kotlin")

    // Selenium
    implementation("org.seleniumhq.selenium:selenium-java")
    implementation("org.seleniumhq.selenium:selenium-devtools-v127:4.23.1")
    implementation("com.squareup.okhttp3:okhttp")

    // WeBDriver
    implementation("io.github.bonigarcia:webdrivermanager:5.8.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")

    // Core Gatling dependencies
    implementation("io.gatling:gatling-core:$gatlingVersion")
    implementation("io.gatling:gatling-http:$gatlingVersion")
    implementation("io.gatling:gatling-app:$gatlingVersion")
    implementation("io.gatling:gatling-charts:$gatlingVersion")
    implementation("io.gatling.highcharts:gatling-charts-highcharts:$gatlingVersion")

    // Other
    implementation("org.slf4j:slf4j-simple:1.7.30")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    doFirst {
        val weaver = configurations["agent"].singleFile
        jvmArgs = listOf("-javaagent:$weaver")
    }
}

tasks.register("generateAndCleanupAllureReport") {
    doFirst {
        val allureResultsDir = file("${project.rootDir}/allure-results")

        if (!allureResultsDir.exists() || allureResultsDir.list()?.isEmpty() == true) {
            println("No allure-results folder found or folder is empty. Skipping report generation.")
            return@doFirst
        }

        exec {
            workingDir = project.rootDir
            if (System.getProperty("os.name").lowercase().contains("windows")) {
                commandLine("cmd", "/c", "allure", "generate", "--single-file", "--clean")
            } else {
                executable("sh")
                args("-c", "allure generate --single-file --clean")
            }
            environment("PATH", System.getenv("PATH"))
        }

        if (allureResultsDir.exists()) {
            allureResultsDir.deleteRecursively()
            logger.info("Successfully deleted allure-results folder")
        }
    }
}

listOf("test", "smokeTest", "regressTest").forEach { taskName ->
    tasks.named(taskName) {
        finalizedBy("generateAndCleanupAllureReport")
    }
}

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

tasks.withType(JavaCompile::class) {
	sourceCompatibility = "${JavaVersion.VERSION_17}"
	targetCompatibility = "${JavaVersion.VERSION_17}"
	options.encoding = "UTF-8"
	options.compilerArgs.add("-parameters")
}

val agent: Configuration by configurations.creating {
	isCanBeConsumed = true
	isCanBeResolved = true
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
		description = "Runs cucumber and junit tests by default test job"
		systemProperties(project.gradle.startParameter.systemPropertiesArgs)
		useJUnitPlatform {
		}
	}
}


tasks.register<Test>("smokeTest") {
	description = "Creates separate job to run both cucumber and junit smoke tests"
	systemProperties(project.gradle.startParameter.systemPropertiesArgs)
	useJUnitPlatform {
//            excludeTags("automated")
		includeTags("smoke")
	}
}

tasks.register<Test>("regressTest") {
	description = "Creates separate job to run both cucumber and junit regression tests"
	systemProperties(project.gradle.startParameter.systemPropertiesArgs)
	useJUnitPlatform {
//            excludeTags("automated")
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
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Cucumber
	implementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))
	implementation("io.cucumber:cucumber-junit-platform-engine")
	implementation("io.cucumber:cucumber-java")
	implementation("io.cucumber:cucumber-spring")

	// Allure
	implementation(platform("io.qameta.allure:allure-bom:$allureVersion"))
	implementation("io.qameta.allure:allure-cucumber7-jvm")
	implementation("io.qameta.allure:allure-junit-platform")

	// JUnit
	implementation(platform("org.junit:junit-bom:5.10.0"))
	implementation("org.junit.platform:junit-platform-suite")
	implementation("org.junit.jupiter:junit-jupiter-api")
	implementation("org.junit.jupiter:junit-jupiter-engine")
	implementation("org.junit.platform:junit-platform-suite:1.9.2")

	implementation("org.slf4j:slf4j-simple:1.7.30")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("generateAndCleanupAllureReport") {
	doLast {
		val allureResultsDir = file("${project.rootDir}/allure-results")

		if (!allureResultsDir.exists() || allureResultsDir.list()?.isEmpty() == true) {
			println("No allure-results folder found or folder is empty. Skipping report generation.")
			return@doLast
		}

		// Use shell environment
		exec {
			workingDir = project.rootDir
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				commandLine("cmd", "/c", "allure", "generate", "--single-file", "--clean")
			} else {
				executable("sh")
				args("-c", "allure generate --single-file --clean")
			}

			// Pass the current PATH environment
			environment("PATH", System.getenv("PATH"))
		}

		if (allureResultsDir.exists()) {
			allureResultsDir.deleteRecursively()
			println("Successfully deleted allure-results folder")
		}
	}
}

listOf("test", "smokeTest", "regressTest").forEach { taskName ->
	tasks.named(taskName) {
		finalizedBy("generateAndCleanupAllureReport")
	}
}

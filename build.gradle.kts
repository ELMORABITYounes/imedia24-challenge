import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.4.32"
	kotlin("jvm") version "1.4.30"
	kotlin("plugin.spring") version "1.4.30"
}

group = "de.imedia24"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
	implementation {
		exclude(module = "spring-boot-starter-logging")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("io.springfox:springfox-boot-starter:3.0.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr353:2.12.3")
	implementation("org.apache.geronimo.specs:geronimo-json_1.1_spec:1.5")
	implementation("org.apache.johnzon:johnzon-core")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.hibernate.validator:hibernate-validator:6.0.13.Final")
	implementation("org.flywaydb:flyway-core")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.h2database:h2:1.4.200")

	runtimeOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

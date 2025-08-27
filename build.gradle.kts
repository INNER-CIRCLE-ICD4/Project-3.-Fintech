import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25" apply false
    id("org.springframework.boot") version "3.5.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false

    kotlin("plugin.jpa") version "1.9.25" apply false
    kotlin("plugin.allopen") version "1.9.25" apply false

    // Kover 플러그인 추가 (Kotlin 코드 커버리지) - 최신 버전
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

allprojects {
    group = "com.sendy"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "kotlin")
    apply(plugin = "java")
    apply(plugin = "java-library")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        runtimeOnly("com.h2database:h2")
        implementation("mysql:mysql-connector-java:8.0.33")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

        testImplementation("com.ninja-squad:springmockk:4.0.2")
        testImplementation("io.mockk:mockk:1.13.10")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        // logback
        // https://tech.kakaopay.com/post/efficient-logging-with-kotlin/
        implementation("ch.qos.logback:logback-classic:1.5.18")

        // tsid
        implementation("com.github.f4b6a3:tsid-creator:5.2.6")

        // monitoring
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("io.micrometer:micrometer-registry-prometheus")

        // mokito
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1") // 최신 버전 확인 가능

        kover(project(":sendy-legacy-api"))
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        reports.junitXml.required.set(true)
        // 테스트 실패해도 계속 진행
        ignoreFailures = true
    }

    // 라이브러리 모듈들은 bootJar 비활성화
    if (project.name in listOf("sendy-shared-kafka", "sendy-shared-mongoDB", "sendy-notification-api", "sendy-notification-producer", "sendy-notificaiton-mongoDB")) {
        tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = false
        }
        tasks.getByName<Jar>("jar") {
            enabled = true
        }
    } else {
        tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
            enabled = true
        }
        tasks.getByName<Jar>("jar") {
            enabled = false
        }
    }

    kover {
        reports {
            filters {
                excludes {
                    // 제외할 클래스들 (필요에 따라 추가)
                    classes("*Application*", "*Config*", "*Test*")
                }
            }
        }
    }
}

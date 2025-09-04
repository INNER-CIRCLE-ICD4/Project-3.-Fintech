group = "com.sendy.sendy-shared-mongo"
version = "0.0.1-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    // MongoDB 관련 의존성을 다른 모듈에 제공
    api("org.springframework.boot:spring-boot-starter-data-mongodb")

    // 테스트용 (라이브러리 내부에서만 사용)
    testImplementation("org.testcontainers:testcontainers:1.21.3")
    testImplementation("org.testcontainers:mongodb:1.24.0")
}

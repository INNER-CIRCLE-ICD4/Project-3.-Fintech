group = "com.sendy.sendy-notification-consumer"
version = "0.0.1-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.bootJar {
    archiveFileName.set("sendy-notification-consumer.jar")
    mainClass.set("com.sendy.consumer.NotificationConsumerApplicationKt")
}

dependencies {
    // MongoDB 공통 모듈 (알림 저장용)
    implementation(project(":sendy-shared-mongo"))

    // Kafka 공통 모듈 (이벤트 수신용)
    implementation(project(":sendy-shared-kafka"))

    // Web 및 Kafka 의존성
    implementation("org.springframework.boot:spring-boot-starter-web")
}

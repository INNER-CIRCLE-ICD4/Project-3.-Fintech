group = "com.sendy.transfer-consumer"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api(project(":sendy-transfer-domain"))
    api(project(":sendy-shared-kafka"))

    testImplementation("org.springframework.kafka:spring-kafka-test")
}

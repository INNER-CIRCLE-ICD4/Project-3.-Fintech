group = "com.sendy.transfer-consumer"
version = "0.0.1-SNAPSHOT"

tasks.bootJar {
    archiveFileName.set("sendy-trasnfer-consumer.jar")
    mainClass.set("com.sendy.transferConsumer.SendyTransferConsumerApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api(project(":sendy-transfer-domain"))
    api(project(":sendy-shared-kafka"))

    testImplementation("org.springframework.kafka:spring-kafka-test")
}

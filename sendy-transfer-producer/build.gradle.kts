group = "com.sendy"
version = "0.0.1-SNAPSHOT"
description = "sendy-transfer-producer"

tasks.bootJar {
    archiveFileName.set("sendy-trasnfer-producer.jar")
    mainClass.set("com.sendy.transferProducer.SendyTransferProducerApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api(project(":sendy-shared-kafka"))
}

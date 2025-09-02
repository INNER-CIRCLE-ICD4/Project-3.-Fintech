group = "com.sendy.user-producer"
version = "0.0.1-SNAPSHOT"
description = "sendy-user-producer"

tasks.bootJar {
    archiveFileName.set("sendy-user-producer.jar")
    mainClass.set("com.sendy.userProducer.SendyUserProducerApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api(project(":sendy-shared-kafka"))
}

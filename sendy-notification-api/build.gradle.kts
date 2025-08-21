group = "com.sendy.sendy-notification-api"
version = "0.0.1-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.bootJar {
    archiveFileName.set("sendy-notification-api.jar")
}

dependencies {
    //MongoDB 공통 모듈 (알림 조회용)
    implementation(project(":sendy-shared-mongoDB"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}

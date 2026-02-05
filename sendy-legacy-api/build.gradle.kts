group = "com.sendy.legacy-api"
version = "0.0.1-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.bootJar {
    archiveFileName.set("sendy-legacy-api.jar")
    mainClass.set("com.sendy.sendyLegacyApi.SendyLegacyApiKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // springSecurity
    implementation("org.springframework.boot:spring-boot-starter-security")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // kafka
    implementation(project(":sendy-shared-kafka"))
}

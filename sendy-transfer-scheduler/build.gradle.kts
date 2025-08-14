group = "com.sendy.transfer-scheduler"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    api(project(":sendy-transfer-domain"))
}

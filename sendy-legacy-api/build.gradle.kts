group = "com.sendy.legacy-api"
version = "0.0.1-SNAPSHOT"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.bootJar {
    archiveFileName.set("sendy-legacy-api.jar")
}

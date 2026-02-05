plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "sendy"

// application
include("sendy-legacy-api", "sendy-shared-kafka", "sendy-transfer-scheduler", "sendy-transfer-consumer")

// library
include("sendy-transfer-domain")
include("sendy-notification-api")
include("sendy-notification-consumer")
include("sendy-notification-producer")
include("sendy-shared-mongo")
include("sendy-user-producer")
include("sendy-transfer-producer")

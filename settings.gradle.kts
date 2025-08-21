rootProject.name = "sendy"

// application
include("sendy-legacy-api", "sendy-shared-kafka", "sendy-transfer-scheduler", "sendy-transfer-consumer")

// library
include("sendy-transfer-domain")

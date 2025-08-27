package com.sendy.sharedMongoDB.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * MongoDB 설정
 * 
 * MongoRepository를 사용하기 위한 설정
 */
@Configuration
@EnableMongoRepositories(basePackages = ["com.sendy.sharedMongoDB"])
class MongoConfig : AbstractMongoClientConfiguration() {
    
    override fun getDatabaseName(): String = "notification"
    
    override fun mongoClient(): MongoClient {
        val mongoUri = System.getenv("SPRING_DATA_MONGODB_URI") 
            ?: "mongodb://sendy-mongoDB:sendy123!@sendy-mongodb:27017/notification?authSource=admin"
        println(" MongoDB 연결 시도: $mongoUri")
        return MongoClients.create(mongoUri)
    }
}
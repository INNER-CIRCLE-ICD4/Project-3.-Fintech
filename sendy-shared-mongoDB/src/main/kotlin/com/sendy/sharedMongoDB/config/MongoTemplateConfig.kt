package com.sendy.sharedMongoDB.config

import org.springframework.context.annotation.Configuration

/**
 * MongoDB 설정
 * Spring Boot가 application.yml 설정을 자동으로 읽어서 
 * MongoClient와 MongoTemplate을 자동 구성합니다.
 * 
 * 별도 @Bean 정의가 필요없슴
 */
@Configuration
class MongoTemplateConfig {

}



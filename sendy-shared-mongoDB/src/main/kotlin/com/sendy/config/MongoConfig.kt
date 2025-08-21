package com.sendy.config

import org.springframework.context.annotation.Configuration

/**
 * MongoDB 자동 구성
 * 
 * Spring Boot가 application.yml의 설정을 자동으로 읽어서
 * MongoClient와 MongoTemplate Bean을 자동 생성합니다.
 * 
 * 별도의 @Bean 정의가 불필요합니다!
 */
@Configuration
class MongoConfig {

}
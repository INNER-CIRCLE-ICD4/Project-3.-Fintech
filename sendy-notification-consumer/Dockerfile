FROM eclipse-temurin:21.0.4_7-jdk-alpine as build
WORKDIR /builder

# Gradle wrapper 및 설정 파일들 복사 (루트 기준)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# 의존 모듈들 복사 (notification-consumer가 필요한 것들)
COPY ../sendy-shared-kafka sendy-shared-kafka/
COPY ../sendy-shared-mongoDB sendy-shared-mongoDB/

# 현재 모듈 복사
COPY . .

RUN chmod +x ./gradlew

# sendy-notification-consumer 모듈만 빌드
RUN ./gradlew :sendy-notification-consumer:bootJar --no-daemon

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

# 빌드된 jar 파일 복사
COPY --from=build /builder/sendy-notification-consumer/build/libs/sendy-notification-consumer.jar app.jar

# 애플리케이션 실행
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
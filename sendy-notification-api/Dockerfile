FROM eclipse-temurin:21.0.4_7-jdk-alpine as build
WORKDIR /builder

# Gradle wrapper 및 설정 파일들 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# 공통 모듈들 복사
COPY sendy-shared-mongo/ sendy-shared-mongo/

# 현재 모듈 복사
COPY . .

RUN chmod +x ./gradlew

# 현재 모듈만 빌드
RUN ./gradlew :sendy-notification-api:bootJar


FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

COPY --from=build /builder/sendy-notification-api/build/libs/sendy-notification-api.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
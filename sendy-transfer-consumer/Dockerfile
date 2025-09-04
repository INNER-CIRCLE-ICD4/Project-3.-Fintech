FROM eclipse-temurin:21.0.4_7-jdk-alpine as build
WORKDIR /builder

# Gradle wrapper 및 설정 파일들 복사 (루트 기준)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# 의존 모듈만 복사 (sendy-legacy-api가 필요한 것만)
COPY sendy-shared-kafka/ sendy-shared-kafka/
COPY sendy-transfer-domain/ sendy-transfer-domain/

# 현재 모듈 복사
COPY . .
RUN chmod +x ./gradlew

# sendy-legacy-api 모듈만 빌드
RUN ./gradlew :sendy-transfer-consumer:bootJar --no-daemon

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

# 빌드된 jar 파일 복사
COPY --from=build /builder/sendy-transfer-consumer/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
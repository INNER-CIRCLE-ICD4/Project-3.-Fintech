FROM eclipse-temurin:21.0.4_7-jdk-alpine as build
WORKDIR /builder

# Gradle wrapper 및 설정 파일들 복사 (루트 기준)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# 의존 모듈만 복사 (sendy-legacy-api가 필요한 것만)
COPY ../sendy-shared-kafka sendy-shared-kafka/

# 현재 모듈 복사
COPY . .

RUN chmod +x ./gradlew

# sendy-legacy-api 모듈만 빌드
RUN ./gradlew :sendy-user-producer:bootJar --no-daemon

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

# 빌드된 jar 파일 복사
COPY --from=build /builder/sendy-user-producer/build/libs/sendy-user-producer.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
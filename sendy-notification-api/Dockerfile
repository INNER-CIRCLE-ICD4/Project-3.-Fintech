FROM eclipse-temurin:21.0.4_7-jdk-alpine as build
WORKDIR /builder

# Gradle wrapper 및 설정 파일들 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./

# 공통 모듈들 복사
COPY sendy-shared-mongoDB/ sendy-shared-mongoDB/
COPY sendy-notificaiton-mongoDB/ sendy-notificaiton-mongoDB/

# 현재 모듈 복사
COPY . .

RUN chmod +x ./gradlew

# 현재 모듈만 빌드
RUN ./gradlew :sendy-notification-api:bootJar

FROM eclipse-temurin:21.0.4_7-jre-alpine as extract
WORKDIR /extract

COPY --from=build /builder/sendy-notification-api/build/libs/sendy-notification-api.jar sendy-notification-api.jar
RUN java -Djarmode=tools -jar sendy-notification-api.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

COPY --from=extract /extract/extracted/dependencies/ ./
COPY --from=extract /extract/extracted/spring-boot-loader/ ./
COPY --from=extract /extract/extracted/snapshot-dependencies/ ./
COPY --from=extract /extract/extracted/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

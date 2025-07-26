FROM eclipse-temurin:21.0.4_7-jre-alpine as build
WORKDIR /builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY src src

RUN chmod +x ./gradlew

RUN ./gradlew bootJar

FROM eclipse-temurin:21.0.4_7-jre-alpine as extract
WORKDIR /extract

COPY --from=build /builder/build/libs/sendy-service.jar sendy-service.jar
RUN java -Djarmode=tools -jar sendy-service.jar extract --layers --destination extracted


FROM eclipse-temurin:21.0.4_7-jre-alpine
WORKDIR /app

COPY --from=extract /extract/sendy-service.jar sendy-service.jar
COPY --from=extract /extract/extracted/dependencies/ ./
COPY --from=extract /extract/extracted/spring-boot-loader/ ./
COPY --from=extract /extract/extracted/snapshot-dependencies/ ./
COPY --from=extract /extract/extracted/application/ ./

ENTRYPOINT ["java","-jar", "sendy-service.jar"]
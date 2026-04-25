FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .

RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/libs/app.jar app.jar
COPY wait-for-mysql.sh /app/wait-for-mysql.sh

RUN chmod +x /app/wait-for-mysql.sh && apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/app/wait-for-mysql.sh"]

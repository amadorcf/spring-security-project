# Configuramos esta imagen de Linux -alpine- ya que es muy tiene un peso liviano, solo tiene lo imprescindible
FROM eclipse-temurin:17-jdk-alpine
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
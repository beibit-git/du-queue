FROM openjdk:17

COPY ./target/dulaty-academy.jar app.jar
#COPY src/main/resources/keystore_dulaty.p12 /app/keystore_dulaty.p12

#RUN /home/gitlab-runner/academy/mvnw dependency:go-offline
#RUN /home/gitlab-runner/academy/mvnw clean install
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""


#ENTRYPOINT ["java", "-jar", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
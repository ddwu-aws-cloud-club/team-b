FROM amazoncorretto:17-alpine-jdk

WORKDIR application
COPY ./dependencies ./
COPY ./spring-boot-loader ./
COPY ./snapshot-dependencies ./
COPY ./application ./

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.JarLauncher"]
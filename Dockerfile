# ---------- Build Stage ----------
FROM maven:3.9.10-eclipse-temurin-17 AS build

WORKDIR /app

# 先复制 pom.xml 利用缓存
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# 再复制源码
COPY src ./src

# 打包
RUN mvn -q -DskipTests package


# ---------- Runtime Stage ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# 定义可变 Jar 文件名
ARG JAR_FILE=target/*.jar

# 从构建阶段拷贝 Jar
COPY --from=build /app/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
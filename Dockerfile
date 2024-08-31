# 使用 OpenJDK 基础镜像
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制生成的 JAR 文件到容器中
COPY target/user-center-backend-0.0.1-SNAPSHOT.jar app.jar

# 设置环境变量以启用生产环境配置
ENV SPRING_PROFILES_ACTIVE=prod

# 暴露应用运行的端口
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]
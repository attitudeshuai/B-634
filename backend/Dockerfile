# 使用Maven构建
FROM maven:3.9-amazoncorretto-17 AS builder

WORKDIR /app

# 复制Maven配置文件（使用阿里云镜像加速）
COPY settings.xml /root/.m2/settings.xml

# 复制pom文件并下载依赖（利用Docker缓存）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM amazoncorretto:17

WORKDIR /app

# 复制构建好的jar文件
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]

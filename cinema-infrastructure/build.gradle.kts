plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("java-test-fixtures") // 테스트 픽스처 활성화
}

group = "com.hanghae"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":cinema-domain")) //도메인 계층 의존성
    implementation(project(":cinema-application")) // 애플리케이션 계층 의존성
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") //db
    runtimeOnly("com.h2database:h2") //db
    runtimeOnly("com.mysql:mysql-connector-j") //db

    implementation("org.springframework.boot:spring-boot-starter-data-redis") //redis
    implementation("org.springframework.boot:spring-boot-starter-cache") //스프링 캐싱
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2") //LocalDate, LocalDateTime 캐시 처리를 위함

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta") //querydsl
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta") //querydsl
    annotationProcessor("jakarta.annotation:jakarta.annotation-api") //querydsl
    annotationProcessor("jakarta.persistence:jakarta.persistence-api") //querydsl

    implementation("org.redisson:redisson-spring-boot-starter:3.43.0") //redisson
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations["annotationProcessor"] // 컴파일 시 Annotation Processor가 동작하도록 설정
}
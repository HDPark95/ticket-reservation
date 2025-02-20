package kr.hhplus.be.server;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfiguration {

	public static final MySQLContainer<?> MYSQL_CONTAINER;
	public static final GenericContainer REDIS;
	public static final ConfluentKafkaContainer KAFKA;
	static {
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
				.withDatabaseName("hhplus")
				.withUsername("test")
				.withPassword("test");
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

		REDIS = new GenericContainer(DockerImageName.parse("redis:latest"))
				.withExposedPorts(6379)
				.withCommand("redis-server", "--requirepass", "test")
				.waitingFor(Wait.forListeningPort());

		REDIS.start();
		System.setProperty("spring.data.redis.host", REDIS.getHost());
		System.setProperty("spring.data.redis.port", String.valueOf(REDIS.getFirstMappedPort()));
		System.setProperty("spring.data.redis.password", "test");


		KAFKA = new ConfluentKafkaContainer("confluentinc/cp-kafka:latest");
		KAFKA.start();
		System.setProperty("spring.kafka.bootstrap-servers", KAFKA.getBootstrapServers());
	}

	@PreDestroy
	public void preDestroy() {
		if (MYSQL_CONTAINER.isRunning()) {
			MYSQL_CONTAINER.stop();
		}
		if (REDIS.isRunning()) {
			REDIS.stop();
		}
		if (KAFKA.isRunning()) {
			KAFKA.stop();
		}
	}
}
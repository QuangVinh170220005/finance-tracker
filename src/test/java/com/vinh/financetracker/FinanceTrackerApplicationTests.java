package com.vinh.financetracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers // Kích hoạt Testcontainers
class FinanceTrackerApplicationTests {

	@Container
	@ServiceConnection // Tự động map các thuộc tính spring.datasource.*
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@Test
	void contextLoads() {
		// Nếu chạy vào đây được là thành công
	}
}
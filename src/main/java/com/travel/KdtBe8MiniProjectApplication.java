package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableCaching
public class KdtBe8MiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KdtBe8MiniProjectApplication.class, args);
	}

}

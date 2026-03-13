package fr.prefecture.sidsic.dashboard_sidsic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DashboardSidsicApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardSidsicApplication.class, args);
	}

}

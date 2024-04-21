package at.schrer.qrbill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class QrbillApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrbillApplication.class, args);
	}

}

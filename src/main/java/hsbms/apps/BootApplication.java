package hsbms.apps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BootApplication {
	
	public static void main(String[] args) {
		log.debug("##### Application Start #####");

		SpringApplication springApplication = new SpringApplication(BootApplication.class);
		//springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		log.debug("##### EgovBootApplication End #####");
	}
}

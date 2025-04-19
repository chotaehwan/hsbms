package hsbms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ServletComponentScan
@SpringBootApplication
@EnableScheduling

//@Import({AppWebApplicationInitializer.class})

public class BootApplication {
	
	public static void main(String[] args) {
		
		log.debug("##### Application Start #####");
		
		SpringApplication springApplication = new SpringApplication(BootApplication.class);
		//springApplication.setBannerMode(Banner.Mode.OFF);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		log.debug("##### Application End #####");
	}
}

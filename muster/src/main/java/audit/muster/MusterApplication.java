package audit.muster;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusterApplication {
	
	@Value("${spring.datasource.url}")
	String databaseURL;
	
	@PostConstruct
    public void init() {
       org.hsqldb.util.DatabaseManagerSwing.main(new String[] { "--url",databaseURL, "--noexit" }); 
    }

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(MusterApplication.class, args);
	}
}

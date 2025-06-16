package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
		
		info = @Info(
				
				title = "Banking Application",
				description = "Rest API for Banking Application",
				contact = @Contact(
						
						name = "Shubham",
						email = "sid.dharth4487@gmail.com",
						url = "https://github.com/shubham07pr"
					),
				    license = @License(
				    		
				    		name = "Banking Application",
				    		url = "https://github.com/shubham07pr"
				    		
				    		)
				
				),
		        externalDocs = @ExternalDocumentation(
		        		
		        		description = "Banking Application documentation",
		        		url = "https://github.com/shubham07pr"
		        		
		        		)
		
		)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
		
	}

}

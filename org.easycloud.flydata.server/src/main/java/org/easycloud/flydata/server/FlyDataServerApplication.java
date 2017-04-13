package org.easycloud.flydata.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "org.easycloud.flydata")
public class FlyDataServerApplication {
	public static void main(String args[]) {
		SpringApplication.run(FlyDataServerApplication.class, args);
	}
}

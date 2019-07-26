package io.epopeia;

import org.jpos.iso.ISOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Iso8583ClientServer {

	public static void main(String... args) throws ISOException {
		SpringApplication.run(Iso8583ClientServer.class, args);
	}
}

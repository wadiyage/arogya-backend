package edu.icet.arogya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ArogyaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArogyaApplication.class, args);
    }
}

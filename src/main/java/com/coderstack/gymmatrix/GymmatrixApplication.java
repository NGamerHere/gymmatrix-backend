package com.coderstack.gymmatrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GymmatrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymmatrixApplication.class, args);
    }

}

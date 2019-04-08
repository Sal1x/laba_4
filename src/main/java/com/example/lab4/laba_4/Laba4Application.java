package com.example.lab4.laba_4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@ComponentScan({"com.example.lab4.laba_4"})
@EntityScan("com.example.lab4.lab_4.domain")
public class Laba4Application {
    public static void main(String[] args) {

        SpringApplication.run(Laba4Application.class, args);

    }

}

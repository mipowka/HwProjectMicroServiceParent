package org.example.mainmicroservicebtcip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MainMicroServiceBtcIpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainMicroServiceBtcIpApplication.class, args);
    }
}


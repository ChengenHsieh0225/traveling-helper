package com.travelinghelper.planning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PlanningApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlanningApplication.class, args);
    }
}

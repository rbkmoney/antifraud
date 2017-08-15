package com.rbkmoney.antifraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.rbkmoney.antifraud"})
public class AntifraudApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(AntifraudApplication.class, args);
    }
}

package com.jdc.web2026i;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jdc.web2026i.util.FixedPortGuard;

@SpringBootApplication
public class Web2026IApplication {

    public static void main(String[] args) {
        FixedPortGuard.reclaimPort();
        SpringApplication.run(Web2026IApplication.class, args);
    }

}

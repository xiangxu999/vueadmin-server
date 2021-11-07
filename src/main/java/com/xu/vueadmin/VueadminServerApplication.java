package com.xu.vueadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class VueadminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueadminServerApplication.class, args);
    }

}

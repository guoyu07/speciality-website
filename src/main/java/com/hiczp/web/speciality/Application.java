package com.hiczp.web.speciality;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by czp on 17-2-7.
 */
@SpringBootApplication
@EnableAdminServer
public class Application {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }
}

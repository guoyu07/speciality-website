package com.hiczp.web.speciality;

import com.hiczp.web.speciality.listener.ReleaseConfigFiles;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by czp on 17-2-7.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Throwable {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .listeners(new ReleaseConfigFiles())
                .run(args);
    }
}

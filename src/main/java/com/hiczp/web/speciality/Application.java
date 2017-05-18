package com.hiczp.web.speciality;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by czp on 17-2-7.
 */
@SpringBootApplication
public class Application {
    private static final String CONFIG_FILE_DIRECTORY = "./config/";
    private static final String[] CONFIG_FILES = new String[]{
            "application.properties",
            "application-dev.properties",
            "application-prod.properties"
    };

    public static void main(String[] args) throws Throwable {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .listeners((ApplicationStartingEvent applicationStartingEvent) ->
                        Stream.of(CONFIG_FILES).forEach(CONFIG_FILE -> {
                            try {
                                String config_file_path = CONFIG_FILE_DIRECTORY + CONFIG_FILE;
                                File file = new File(config_file_path);
                                if (!file.exists()) {
                                    FileUtils.copyURLToFile(new ClassPathResource(config_file_path).getURL(), file);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                ).run(args);
    }
}

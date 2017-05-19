package com.hiczp.web.speciality.listener;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by czp on 17-5-19.
 */
public class ReleaseConfigFiles implements ApplicationListener<ApplicationStartingEvent> {
    private static final String CONFIG_FILE_DIRECTORY = "./config/";
    private static final String[] CONFIG_FILES = new String[]{
            "application.properties",
            "application-dev.properties",
            "application-prod.properties"
    };

    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        Stream.of(CONFIG_FILES).forEach(configFileName -> {
            try {
                String configFilePath = CONFIG_FILE_DIRECTORY + configFileName;
                File file = new File(configFilePath);
                if (!file.exists()) {
                    FileUtils.copyURLToFile(new ClassPathResource(configFilePath).getURL(), file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

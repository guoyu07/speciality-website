package com.hiczp.web.speciality.controller.api.admin;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by czp on 17-4-29.
 */
@RestController
@RequestMapping("/api/admin/upload")
public class FileUploadAPIController {
    private static final String ARTICLE_IMAGES_UPLOAD_LOCATION = "./resources/images/article/";

    @PostMapping("/article")
    public String[] articleFileUpload(MultipartFile[] multipartFiles) {
        String[] names = new String[multipartFiles.length];
        MultipartFile multipartFile;
        String originalFilename;
        String suffix;
        String name;
        File file;
        for (int i = 0; i < multipartFiles.length; i++) {
            multipartFile = multipartFiles[i];
            if (!multipartFile.getContentType().startsWith("image/")) {
                continue;
            }
            originalFilename = multipartFile.getOriginalFilename();
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            do {
                name = String.format("%s %s%s", new Date().toString(), new Random().nextInt(), suffix);
                file = new File(ARTICLE_IMAGES_UPLOAD_LOCATION + name);
            } while (file.exists());
            try {
                FileUtils.copyToFile(multipartFiles[i].getInputStream(), file);
                names[i] = name;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return names;
    }
}

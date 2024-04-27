package com.mymerit.mymerit.domain.service;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public String getBase64ImageURL(String imageURL) {
        String imageBase64 = null;

        try {
            URL url = new URL(imageURL);

            InputStream inputStream = url.openStream();

            byte[] bytes = IOUtils.toByteArray(inputStream);

            imageBase64 = Base64.encodeBase64String(bytes);
        }
        catch (IOException e) {
            logger.error("Error while converting imageURL to base64", e);
        }

        return imageBase64;
    }

    public String getBase64Image(MultipartFile file) {
        String imageBase64 = null;

        try {
            byte[] bytes = file.getBytes();

            imageBase64 = Base64.encodeBase64String(bytes);
        }
        catch (IOException e) {
            logger.error("Error while converting image to base64", e);
        }

        return imageBase64;
    }
}

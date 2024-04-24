package com.mymerit.mymerit.domain.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Service
public class ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);

    public String getImageBase64FromImageURL(String imageURL) {
        String base64Image = null;

        try {
            URL url = new URL(imageURL);

            InputStream inputStream = url.openStream();

            byte[] bytes = IOUtils.toByteArray(inputStream);

            base64Image = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            logger.error("Error while converting image (URL) to base64", e);
        }

        return base64Image;
    }

    public String getImageBase64FromFile(MultipartFile file) {
        String base64Image = null;

        try {
            byte[] bytes = file.getBytes();

            base64Image = Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            logger.error("Error while converting image (file) to base64", e);
        }

        return base64Image;
    }
}
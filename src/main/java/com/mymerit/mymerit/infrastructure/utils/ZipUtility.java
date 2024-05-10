package com.mymerit.mymerit.infrastructure.utils;


import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.domain.entity.ConfigFile;
import com.mymerit.mymerit.domain.entity.SolutionFile;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import lombok.Data;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtility {



    public static String zipSolutionFilesAsBase64(SolutionRequest solutionRequest, ProgrammingLanguage language) throws IOException {
        if (solutionRequest == null || solutionRequest.getFiles() == null || solutionRequest.getFiles().isEmpty()) {
            throw new IllegalArgumentException("Solution request is null or empty.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            List<SolutionFile> files = solutionRequest.getFiles();
            for (SolutionFile file : files) {
                addToZip(zos, file.getContent().getBytes(), file.getName());
            }

            System.out.println(solutionRequest.getMainName());
            ConfigFile configFile = getSourceFileForLanguage(language, solutionRequest.getMainName());
        
            addToZip(zos, configFile.getCompile().getBytes(), "compile");
            addToZip(zos, configFile.getRun().getBytes(), "run");

        }
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    public static String getFileExtension(String fileName) {
        String[] parts = fileName.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1] : "plaintext";
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex != -1 ? fileName.substring(0, lastDotIndex) : fileName;
    }



    public static ConfigFile getSourceFileForLanguage(ProgrammingLanguage language, String mainFileName) {// porownac mianfile z mutlipart main i indeks wziac i guess
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/extension-scripts-map.json")) {

            Object obj = jsonParser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject languageConfig = (JSONObject) jsonObject.get(language.toString().toLowerCase());
            if (languageConfig != null) {

                String compile = (String) languageConfig.get("compile");
                String run = (String) languageConfig.get("run");
                String source_file_name = (String) languageConfig.get("source_file");

                String compileScriptContent = compile != null ? compile.replace(source_file_name, " *." + getFileExtension(mainFileName)).replace(" %s ", "") : null;
                String runScriptContent = run != null ? run.replace(getFileNameWithoutExtension(source_file_name), getFileNameWithoutExtension(mainFileName)) : null;

                return new ConfigFile(compileScriptContent, runScriptContent);
            } else {
                throw new RuntimeException("No configuration for language: " + language);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON not found: " + e.getMessage());
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error while parsing JSON file: " + e.getMessage());
        }
    }


    private static void addToZip(ZipOutputStream zos, byte[] content, String sourceFile) throws IOException {
        if (sourceFile != null) {
            System.out.println(new String(content, StandardCharsets.UTF_8));
            ZipEntry entry = new ZipEntry(sourceFile);
            zos.putNextEntry(entry);
            zos.write(content);
            zos.closeEntry();
        }
    }

}

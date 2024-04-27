package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.GridFileResponse;
import com.mymerit.mymerit.domain.entity.GridFile;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.service.GridFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.mymerit.mymerit.api.payload.response.ApiResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController 
@RequestMapping("file/")
public class GridFileController {

    @Autowired
    private GridFileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<GridFileResponse> download(@PathVariable String id) throws IOException {
        GridFile loadFile = fileService.gridFile(id);

        GridFileResponse response = new GridFileResponse(loadFile.getFilename(), loadFile.getFileType(), loadFile.getFile());
        System.out.println(response);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getName() + "\"")
                .body(response);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) throws IOException {
        fileService.DeleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
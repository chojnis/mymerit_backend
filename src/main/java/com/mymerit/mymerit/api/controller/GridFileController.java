package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.FileListRequest;
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
@Tag(name = "GridFileController")
public class GridFileController {

    @Autowired
    private GridFileService fileService;

    @Operation(){
        summary = "upload file to mongodb gridfile"
    }
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(fileService.addFile(file), HttpStatus.OK);
    }

    @Operation(){
        summary = "download one file from mongodb gridfile"
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

    @Operation(){
        summary = "download multiple files from mongodb gridfile"
    }
    @PostMapping("/download/")
    public ResponseEntity<List<GridFileResponse>> download(@RequestBody FileListRequest request){
        return ResponseEntity.ok()
                .body(fileService.downloadFiles(request.getFileIDS()));
    }

    @Operation(){
        summary = "delete file from mongodb gridfile"
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) throws IOException {
        fileService.DeleteFile(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
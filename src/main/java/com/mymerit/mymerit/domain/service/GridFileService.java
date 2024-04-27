package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.GridFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.infrastructure.repository.SolutionRepository;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GridFileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public ObjectId addFile(MultipartFile upload) throws IOException {
        System.out.println(upload.getContentType());
        System.out.println(upload);
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        return template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);
    }

    public GridFile gridFile(String id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        GridFile gridFile = new GridFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            gridFile.setFilename(gridFSFile.getFilename());
            gridFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            gridFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
            gridFile.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return gridFile;
    }

    public void DeleteFile(String id) throws IOException {

        template.delete(new Query(Criteria.where("_id").is(id)));

    }

}
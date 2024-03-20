package com.mymerit.mymerit.domain.service;

import com.mymerit.mymerit.domain.entity.DownloadFile;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DownloadFileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    public String addFile(MultipartFile upload) throws IOException {

        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", upload.getSize());

        Object fileID = template.store(upload.getInputStream(), upload.getContentType(), metadata);

        return fileID.toString();
    }
  

    public DownloadFile downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(id)) );

        DownloadFile downloadFile = new DownloadFile();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            downloadFile.setFilename( gridFSFile.getFilename() );

            downloadFile.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );

            downloadFile.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );

            downloadFile.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }

        return downloadFile;
    }

    public void DeleteFile(String id) throws IOException {

        template.delete(new Query(Criteria.where("_id").is(id)));

    }

}
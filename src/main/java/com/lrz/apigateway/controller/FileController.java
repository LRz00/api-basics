/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.apigateway.controller;

import com.lrz.apigateway.data.vo.v1.UploadFileResponseVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lrz.apigateway.services.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
/**
 *
 * @author lara
 */
@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {
    
    private Logger logger = Logger.getLogger(FileController.class.getName());
    
    @Autowired
    private FileStorageService service;
    
    @PostMapping("/upload")
    public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file){
        logger.info("Storing file to disk");
        var filename = service.storeFile(file);
        
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/file/v1/download/")
                .path(filename).toUriString();
        
        return new UploadFileResponseVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }
}

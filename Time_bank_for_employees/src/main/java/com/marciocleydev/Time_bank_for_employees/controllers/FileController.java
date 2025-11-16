package com.marciocleydev.Time_bank_for_employees.controllers;

import com.marciocleydev.Time_bank_for_employees.DTO.UploadFileResponseDTO;
import com.marciocleydev.Time_bank_for_employees.controllers.docs.FileControllerDocs;
import com.marciocleydev.Time_bank_for_employees.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController implements FileControllerDocs {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private FileStorageService service;

    public FileController(FileStorageService service) {
        this.service = service;
    }

    @PostMapping("/uploadFile")
    @Override
    public UploadFileResponseDTO uploadFile(@RequestPart("file") MultipartFile file) {//@RequestPart deixa mais explícito que é multipart.
        var fileName = service.storeFile(file);

        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDTO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    @Override
    public List<UploadFileResponseDTO> uploadMultipleFile(@RequestPart("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> {
                    var fileName = service.storeFile(file);
                    var uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/files/downloadFile/")
                            .path(fileName)
                            .toUriString();

                    return new UploadFileResponseDTO(fileName, uri, file.getContentType(), file.getSize());
                })
                .toList();
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName);
        String contentType = null;

        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException e) {
            LOGGER.error("Could not determine file type.");
        }

        if (contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

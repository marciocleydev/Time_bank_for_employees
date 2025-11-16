package com.marciocleydev.Time_bank_for_employees.services;

import com.marciocleydev.Time_bank_for_employees.config.FileStorageConfig;
import com.marciocleydev.Time_bank_for_employees.exceptions.StoredFileNotFoundException;
import com.marciocleydev.Time_bank_for_employees.exceptions.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        //pega o caminho do diretório de upload (string) e:
        //Paths.get(...): transforma em um Path.
        //toAbsolutePath(): converte para um caminho absoluto.
        //normalize(): remove elementos redundantes (como "." e "..") para uma forma canônica do caminho.
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        try {
            LOGGER.info("Creating directory: {}", this.fileStorageLocation);
            //Cria o diretório e quaisquer diretórios pai necessários, se ainda não existirem. Se já existir, não faz nada (sem erro)
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException e) {
            LOGGER.error("Could not create the directory where the uploaded files will be stored.", e);
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    //Method público para salvar um arquivo enviado (MultipartFile é um tipo do Spring que representa um arquivo enviado via HTTP multipart/form-data).
    public String storeFile(MultipartFile file) {
        //remove segmentos como "../" e limpa caracteres de separação do caminho.
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // 1. Validar nome vazio/nulo
        if (fileName.isBlank()) {
            throw new FileStorageException("Invalid file name.");
        }
        // 2. Bloquear extensões perigosas
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".exe") || lower.endsWith(".bat") || lower.endsWith(".sh")) {
            throw new FileStorageException("Executable files are not allowed.");
        }
        //2.5 verificação do tipo MIME
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new FileStorageException("Only image files are allowed.");
        }
        // 3. Gerar nome único para evitar sobrescrita
        String uniqueName = UUID.randomUUID() + "_" + fileName;

        try (InputStream input = file.getInputStream()) {

            if (fileName.contains("..")) {
                LOGGER.error("Sorry! Filename contains invalid path sequence{} " , fileName);
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            LOGGER.info("Saving file in disk, fileName: {}", uniqueName);
            Path targetLocation = this.fileStorageLocation.resolve(uniqueName);

            //Copia o conteúdo do MultipartFile (via file.getInputStream()) para o targetLocation no disco.
            // O StandardCopyOption.REPLACE_EXISTING permite sobrescrever um arquivo com o mesmo nome se já existir.
            Files.copy(input, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return uniqueName;
        } catch (IOException e) {
            LOGGER.error("Could not store file {}. Please try again!", fileName , e);
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    //Method para ler um arquivo salvo e retornar um Resource (tipo do Spring que representa um recurso legível
    // — pode ser um arquivo, URL, stream etc
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (!filePath.startsWith(this.fileStorageLocation)) {
                throw new FileStorageException("Attempt to access file outside allowed directory.");
            }
            //Cria um UrlResource apontando para o URI do arquivo. UrlResource funciona para file:// URIs e outros protocolos.
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new StoredFileNotFoundException("File not found " + fileName);
            }
        } catch (IOException e) {
            throw new StoredFileNotFoundException("File not found " + fileName, e);
        }
    }
}

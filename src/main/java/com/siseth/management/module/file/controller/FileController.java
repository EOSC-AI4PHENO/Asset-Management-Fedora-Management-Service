package com.siseth.management.module.file.controller;

import com.siseth.management.module.file.dto.file.request.FileIdReqDTO;
import com.siseth.management.module.file.dto.file.response.FileContentResDTO;
import com.siseth.management.module.file.dto.file.response.FileWithPageResDTO;
import com.siseth.management.module.file.service.FileService;
import com.siseth.management.module.internal.api.requests.AddTagToFileReqDTO;
import com.siseth.management.module.internal.api.requests.CreateTagReqDTO;
import com.siseth.management.module.internal.api.response.CreateTagResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/fedora")
public class FileController {

    private final FileService service;

    @GetMapping("/public/file/{fileId}/image")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<byte[]> getFilePublic(@PathVariable Long fileId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers)
                .body(service.getImage(fileId));
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getManualPFD(@RequestParam String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers)
                .body(service.getPdf(name));
    }

    @PostMapping("/zip")
    @SneakyThrows
    public ResponseEntity<?> zipFiles(@RequestBody List<Long> ids) {
        File file = service.zipFiles(ids);
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @GetMapping("/source")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<FileWithPageResDTO> getFileImageToSource(
                                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                                              @RequestParam(required = false, defaultValue = "100000") Integer size,
                                                              @RequestParam(required = false, defaultValue = "") String sort,
                                                              @RequestParam(required = false) String imageSize,
                                                              @RequestParam(required = false, defaultValue = "false") boolean previewPhotoOnly,
                                                              @RequestParam(required = false, defaultValue = "0") String tagIds,
                                                              @RequestParam Long sourceId,
                                                              @RequestParam(required = false) String from,
                                                              @RequestParam(required = false) String to,
                                                              @Parameter(hidden = false) @RequestHeader(required = false)  String id) {
        //TODO dodać realm
    return ResponseEntity.ok(service.getFileImageToSource(sourceId, from, to, imageSize, previewPhotoOnly, page, size, sort, id, tagIds));
    }
    @GetMapping("/ws/test")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> testWS() {
        return ResponseEntity.ok(service.testWebSocket());
    }


    @GetMapping("/getPublicFile/{path}")
    @Operation(summary = "Get image", description = "Get image for the specified path")
    public ResponseEntity<byte[]> getPublicFile(@PathVariable String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return ResponseEntity.ok().headers(headers)
                .body(service.getPublicImage(path));
    }

    @GetMapping("/file/{fileId}/image")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers)
                .body(service.getImage(fileId));
    }

    @GetMapping("/file/{fileId}/image/base64")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<FileContentResDTO> getFileBase64(@PathVariable Long fileId) {
        return ResponseEntity.ok().body(service.getImageBase64(fileId));
    }

    @GetMapping("/file/image/base64")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<FileContentResDTO> getImageBase64(@RequestBody FileIdReqDTO file) {
        return ResponseEntity.ok().body(service.getImageBase64(file.getId()));
    }

    @GetMapping("/file/image")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<byte[]> getFile(@RequestParam String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers)
                .body(service.getImage(path));
    }

    @DeleteMapping("file")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteFiles(@RequestBody List<Long> ids,
                                            @Parameter(hidden = true) @RequestHeader(required = false)  String id,
                                            @Parameter(hidden = true) @RequestHeader(required = false, defaultValue = "") String roles,
                                            @Parameter(hidden = true) @RequestHeader(required = false)  String realm) {
        service.deleteFiles(ids, id, roles, realm);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/source")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteImagesFromSource(
            @RequestParam Long sourceId,
            @RequestParam(required = false, defaultValue = "1000-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false, defaultValue = "3000-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate to,
            @Parameter(hidden = true) @RequestHeader(required = false)  String id,
            @Parameter(hidden = true) @RequestHeader(required = false, defaultValue = "") String roles,
            @Parameter(hidden = true) @RequestHeader(required = false)  String realm) {
        service.deleteImagesFromSource(sourceId, from, to, id, roles, realm);
        return ResponseEntity.ok().build();
    }

}

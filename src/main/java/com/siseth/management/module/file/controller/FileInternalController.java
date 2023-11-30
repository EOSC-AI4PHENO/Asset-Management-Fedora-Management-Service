package com.siseth.management.module.file.controller;

import com.siseth.management.component.fedora.PathGenerator;
import com.siseth.management.module.fedora.api.FedoraFileExistsDTO;
import com.siseth.management.module.file.dto.file.response.FileShortResDTO;
import com.siseth.management.module.file.dto.file.response.FilesSizeDTO;
import com.siseth.management.module.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.http.util.Asserts;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/assets/fedora")
public class FileInternalController {

    private final FileService service;

    @GetMapping("/file/source/{sourceId}/lastDate")
    public ResponseEntity<LocalDateTime> getLastPhotoDateToSourceId(@PathVariable Long sourceId) {
        return ResponseEntity.ok(service.getLastPhotoDate(sourceId));
    }

    @PostMapping("/file/source/{sourceId}/dates")
    public ResponseEntity<Map<LocalDateTime, Boolean>> checkTimesToSource(@PathVariable Long sourceId,
                                                                  @RequestBody List<LocalDateTime> times) {
        return ResponseEntity.ok(service.checkTimesToSource(sourceId,times));
    }

    @PostMapping("/roi")
    public ResponseEntity<FedoraFileExistsDTO> uploadRoi(@RequestPart MultipartFile file,
                                            @RequestParam(required = false) Long sourceId,
                                            @RequestParam String name,
                                            @RequestParam(required = false) String userId,
                                            @RequestParam(required = true, defaultValue = "json_roi") String type,
                                            @RequestParam String realm) {
        return ResponseEntity.ok(service.uploadImage(file, null, userId, sourceId, name, type,  realm, null));
    }

    @PostMapping("/image")
    public ResponseEntity<FedoraFileExistsDTO> uploadImage(@RequestPart MultipartFile file,
                                                           @RequestParam String directory,
                                                           @RequestParam Long sourceId,
                                                           @RequestParam String name,
                                                           @RequestParam(required = false, defaultValue = "image") String type,
                                                           @RequestParam(required = false) String originCreatedAt,
                                                           @RequestParam(required = false) String userId,
                                                           @RequestParam String realm) {
        return ResponseEntity.ok(service.uploadImage(file, directory, userId, sourceId, name, type, realm, originCreatedAt));
    }

    @GetMapping("/image/check")
    public ResponseEntity<Boolean> checkImageExists(@RequestParam String directory,
                                                    @RequestParam Long sourceId,
                                                    @RequestParam String name,
                                                    @RequestParam(required = false, defaultValue = "image") String type,
                                                    @RequestParam String realm) {
        return ResponseEntity.ok(service.checkImageExists(directory, sourceId, name, type, realm));

    }

    @GetMapping("roi")
    public ResponseEntity<byte[]> getRoi(@RequestParam Long fileId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                                .headers(headers)
                                .body(service.getImage(fileId));
    }

    @GetMapping("roi/byte")
    public ResponseEntity<byte[]> getRoiByte(@RequestParam Long fileId) {
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok()
                .headers(headers)
                .body(service.getImage(fileId));
    }

    @GetMapping("/file/source/{sourceId}")
    public ResponseEntity<List<FileShortResDTO>> getFilesImageToSource(@PathVariable Long sourceId,
                                                                        @RequestParam LocalDate dateFrom,
                                                                        @RequestParam LocalDate dateTo,
                                                                        @RequestParam String realm) {
        return ResponseEntity.ok(service.getFileImageToSource(sourceId,dateFrom,dateTo, realm));
    }

    @GetMapping("/file/img/{imgId}")
    public ResponseEntity<FileShortResDTO> getFilesImageDataById(@PathVariable Long imgId) {
        return ResponseEntity.ok(service.getFileById(imgId));
    }


    @GetMapping("/file/{fileId}/image")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers)
                .body(service.getImage(fileId));
    }
    @GetMapping("/file/source/{imageId}/previewPhoto")
    public ResponseEntity<String> getPreviewPhotoForImageId(@PathVariable Long imageId) {
        return ResponseEntity.ok(service.getPreviewPhoto(imageId));
    }

    @GetMapping(value = "/size")
    public ResponseEntity<FilesSizeDTO> getSizeByOwner(@RequestParam String owner,
                                                       @RequestParam String realm,
                                                       @RequestParam Long sourceId){
        return ResponseEntity.ok(service.getSizeByOwner(owner, realm, sourceId));
    }

    @GetMapping("/file/source/betweenDates")
    public ResponseEntity<List<Long>> getSourcesIds(@RequestParam String realm,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate dateFrom,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate dateTo){
        return ResponseEntity.ok(service.getSourcesIds(realm, dateFrom, dateTo));
    }

}

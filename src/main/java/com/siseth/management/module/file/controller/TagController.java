package com.siseth.management.module.file.controller;

import com.siseth.management.component.entity.D_Tag;
import com.siseth.management.module.file.dto.file.response.FileWithPageResDTO;
import com.siseth.management.module.file.dto.file.response.TagResDTO;
import com.siseth.management.module.file.service.FileService;
import com.siseth.management.module.file.service.TagService;
import com.siseth.management.module.internal.api.requests.AddTagToFileReqDTO;
import com.siseth.management.module.internal.api.requests.AddTagsToFilesReqDTO;
import com.siseth.management.module.internal.api.requests.CreateTagReqDTO;
import com.siseth.management.module.internal.api.requests.UpdateTagReqDTO;
import com.siseth.management.module.internal.api.response.CreateTagResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/fedora")
public class TagController {

    private final TagService service;

    @GetMapping("/tags/source")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<TagResDTO>> getSourceTags(@RequestParam Long sourceId,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.getSourceTag(sourceId, id, realm));
    }

    @PostMapping("/createTag")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create tag", description = "Create tag that can be later assign to the image")
    public ResponseEntity<CreateTagResDTO> createTag(@RequestBody CreateTagReqDTO dto,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                                     @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.createTag(dto, id, realm));
    }

    @PostMapping("/addTagToFile")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add tag to a file", description = "Add tag to a file")
    public ResponseEntity<String> addTagToFile(@RequestBody AddTagToFileReqDTO dto,
                                               @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                               @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.addTagToFile(dto, id, realm));
    }

    @PostMapping("/addTagsToFiles")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add tag to a file", description = "Add tag to a file")
    public ResponseEntity<String> addTagsToFiles(@RequestBody AddTagsToFilesReqDTO dto,
                                               @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                               @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.addTagsToFiles(dto, id, realm));
    }

    @PutMapping("/editTag")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Edit tag", description = "Edit tag")
    public ResponseEntity<UpdateTagReqDTO> editTag(@RequestBody UpdateTagReqDTO dto,
                                         @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                         @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                         @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                         @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.editTag(dto, id, realm));
    }

    @DeleteMapping("/removeTag")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove tag", description = "Remove tag")
    public ResponseEntity<String> removeTag(@RequestParam Long tagId,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.removeTag(tagId, id, realm));
    }

    @DeleteMapping("/removeTagFromFile")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Remove tag", description = "Remove tag")
    public ResponseEntity<String> removeTagFromFile(@RequestParam Long tagId,
                                                    @RequestParam Long fileId,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.removeTagFromFile(tagId, fileId, id, realm));
    }

    @GetMapping("/getUserTags")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, List<TagResDTO>>> getUserTags(@Parameter(hidden = true) @RequestHeader(required = false) String id,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                            @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.getUserTags(id, realm));
    }

    @GetMapping("/getFileTags")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, List<TagResDTO>>> getFileTags(@RequestParam Long fileId,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String id,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String token,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String name,
                                                                    @Parameter(hidden = true) @RequestHeader(required = false) String realm) {
        return ResponseEntity.ok(service.getFileTags(fileId, id, realm));
    }
}

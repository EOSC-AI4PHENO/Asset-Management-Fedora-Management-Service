package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileShortResDTO {

    private Long id;

    private Long sourceId;

    private String name;

    private String path;

    private String directory;

    private LocalDateTime originCreatedAt;


    public FileShortResDTO(D_File file) {
        this.id = file.getId();
        this.sourceId = file.getSourceId();
        this.name = file.getName();
        this.path = file.getPath();
        this.directory = file.getDirectory();
        this.originCreatedAt = file.getOriginCreatedAt();
    }

}

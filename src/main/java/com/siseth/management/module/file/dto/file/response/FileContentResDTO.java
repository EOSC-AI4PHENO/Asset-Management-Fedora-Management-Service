package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileContentResDTO {

    private Long id;

    private String name;
    private LocalDateTime createdAt;

    private String content;


    public FileContentResDTO(D_File file, String content) {
        this.id = file.getId();
        this.name = file.getName();
        this.createdAt = file.getOriginCreatedAt();
        this.content = content;
    }

}

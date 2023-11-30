package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.component.entity.D_Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagResDTO {

    private Long id;

    private String name;

    private OffsetDateTime createdAt;

    private OffsetDateTime modifiedAt;

    public TagResDTO(D_Tag dTag) {
        this.id = dTag.getId();
        this.name = dTag.getName();
        this.createdAt = dTag.getCreatedAt();
        this.modifiedAt = dTag.getModifiedAt();
    }
}

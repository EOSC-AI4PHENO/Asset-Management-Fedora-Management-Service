package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilesSizeDTO {

    private Long filesCount;
    private Long size;

}

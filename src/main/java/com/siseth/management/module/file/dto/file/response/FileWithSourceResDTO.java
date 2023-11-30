package com.siseth.management.module.file.dto.file.response;

import com.siseth.management.component.entity.D_File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileWithSourceResDTO extends FileShortResDTO{

    private String content;

    private List<TagResDTO> tags;


    public FileWithSourceResDTO(D_File file, String content, String userId) {
        this(file);

        this.content = content;
        this.tags = file.getFile2TagList().stream()
                .filter(f2t -> f2t.isUserTag(userId))
                .map(x -> new TagResDTO(x.getTag()))
                .collect(Collectors.toList());
    }

    public FileWithSourceResDTO(D_File file) {
        super(file);
    }

}

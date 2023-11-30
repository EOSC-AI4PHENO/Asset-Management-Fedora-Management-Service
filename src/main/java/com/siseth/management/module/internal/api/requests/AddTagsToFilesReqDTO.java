package com.siseth.management.module.internal.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AddTagsToFilesReqDTO {
    private List<Long> fileIds;
    private List<Long> tagIds;
}

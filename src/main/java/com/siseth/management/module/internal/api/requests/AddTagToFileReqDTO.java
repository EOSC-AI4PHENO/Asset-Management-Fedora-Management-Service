package com.siseth.management.module.internal.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddTagToFileReqDTO {
    private Long fileId;
    private Long tagId;
}

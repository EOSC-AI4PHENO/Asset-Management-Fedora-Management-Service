package com.siseth.management.module.internal.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateContainerReqDTO {
    private String path;

    private String name;
}

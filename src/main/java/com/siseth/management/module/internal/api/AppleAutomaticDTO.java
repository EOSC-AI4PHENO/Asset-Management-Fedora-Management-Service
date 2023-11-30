package com.siseth.management.module.internal.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppleAutomaticDTO {

    private String imageBase64;
    private String filename;
    private String jsonBase64ImageROIs;
}

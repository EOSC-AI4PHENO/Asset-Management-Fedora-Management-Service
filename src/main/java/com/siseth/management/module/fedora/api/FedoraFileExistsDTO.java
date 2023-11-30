package com.siseth.management.module.fedora.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FedoraFileExistsDTO {

    private Long fileId;

    private Boolean isExist;

}

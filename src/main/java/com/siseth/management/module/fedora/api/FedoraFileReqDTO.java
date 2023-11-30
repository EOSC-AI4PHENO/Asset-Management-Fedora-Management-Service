package com.siseth.management.module.fedora.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FedoraFileReqDTO {

    private String name;

    private String directory;

    private String type;

    private String owner;

    private Long sourceId;

    private String origin;

}

package com.siseth.management.module.internal.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {

    private Long lat;
    private Long lon;
    private String utcDate;
}

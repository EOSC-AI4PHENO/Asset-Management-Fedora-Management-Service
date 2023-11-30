package com.siseth.management.module.internal.api.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTagReqDTO extends CreateTagReqDTO{

    private Long id;
}

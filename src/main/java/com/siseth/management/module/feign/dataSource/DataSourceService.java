package com.siseth.management.module.feign.dataSource;

import com.siseth.management.module.feign.dataSource.dto.SourceInternalShortResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "source-adapter-service")
public interface DataSourceService {

    @GetMapping("/api/internal/digital/source-adapter/ids")
    List<SourceInternalShortResDTO> getAllByIds(@RequestParam List<Long> ids);

    @GetMapping("/api/internal/digital/source-adapter/{id}/get")
    SourceInternalShortResDTO getSource(@PathVariable Long id);

}

package com.siseth.management.module.version;

import com.siseth.management.constant.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets/fedora/version")
public class VersionController {

    @GetMapping
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(AppConstant.VERSION);
    }

}

package com.siseth.management.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConstant {

    public static String VERSION;
    public static String FEDORA_URL;

    @Value("${app.fedora.url}")
    public void setUrl(String url) {
        FEDORA_URL = url;
    }

    @Value("${app.version}")
    public void setVersion(String version) {
        VERSION = version;
    }

}

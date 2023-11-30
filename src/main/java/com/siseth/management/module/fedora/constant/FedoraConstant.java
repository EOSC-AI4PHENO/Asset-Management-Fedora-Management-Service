package com.siseth.management.module.fedora.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FedoraConstant {

    public static String FEDORA_URL;

    public static String FEDORA_LOGIN;

    public static String FEDORA_PASSWORD;

    @Value("${app.fedora.url}")
    public void setUrl(String url) {
        FEDORA_URL = url;
    }

    @Value("${app.fedora.login}")
    public void setLogin(String login) {
        FEDORA_LOGIN = login;
    }

    @Value("${app.fedora.password}")
    public void setPassword(String password) {
        FEDORA_PASSWORD = password;
    }

}

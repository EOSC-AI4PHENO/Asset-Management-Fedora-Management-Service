package com.siseth.management.module.fedora.model.common;

import com.siseth.management.module.fedora.constant.FedoraConstant;

import java.util.Base64;

public class FedoraToken {

    private final String login = FedoraConstant.FEDORA_LOGIN;

    private final String password = FedoraConstant.FEDORA_PASSWORD;

    public String getToken(){
        String valueToEncode = login + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}

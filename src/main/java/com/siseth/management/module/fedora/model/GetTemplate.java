package com.siseth.management.module.fedora.model;

import com.siseth.management.constant.AppConstant;
import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;

@AllArgsConstructor
public class GetTemplate extends HttpClientFedora {

    private String path;

    @SneakyThrows
    @Override
    public String getResponse() {
        String token = new FedoraToken().getToken();
        HttpClient client = builder();
        HttpGet request = new HttpGet();
        request.addHeader("Authorization", token);

        request.setURI(URI.create(AppConstant.FEDORA_URL + path));
        HttpResponse response = client.execute(request);
        return decode(response.getEntity());

    }
}

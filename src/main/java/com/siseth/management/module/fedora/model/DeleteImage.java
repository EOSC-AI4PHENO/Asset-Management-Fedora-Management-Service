package com.siseth.management.module.fedora.model;

import com.siseth.management.component.image.ImageUtil;
import com.siseth.management.constant.AppConstant;
import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DeleteImage extends HttpClientFedora {

    private String path;

    @SneakyThrows
    @Override
    public String getResponse() {
        String token = new FedoraToken().getToken();
        HttpClient client = builder();
        HttpDelete request = new HttpDelete();
        request.addHeader("Authorization", token);
        this.path = ImageUtil.escapeString(this.path);

        request.setURI(URI.create(AppConstant.FEDORA_URL + path));
        client.execute(request);

        request.setURI(URI.create(AppConstant.FEDORA_URL + path + "/fcr:tombstone"));
        client.execute(request);
        return "OK";

    }
}

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
import org.apache.http.client.methods.HttpGet;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class GetImage extends HttpClientFedora {

    private String path;

    @SneakyThrows
    @Override
    public byte[] getResponse() {
        String token = new FedoraToken().getToken();
        HttpClient client = builder();
        HttpGet request = new HttpGet();
        request.addHeader("Authorization", token);
        List<byte[]> arr = new ArrayList<>();
        this.path = ImageUtil.escapeString(this.path);


        request.setURI(URI.create(AppConstant.FEDORA_URL + path));
        HttpResponse response = client.execute(request);
        return IOUtils.toByteArray(response.getEntity().getContent());

    }
}

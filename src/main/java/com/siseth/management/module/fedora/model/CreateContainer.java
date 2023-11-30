package com.siseth.management.module.fedora.model;

import com.siseth.management.module.fedora.constant.FedoraConstant;
import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import com.siseth.management.module.internal.api.requests.CreateContainerReqDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

@AllArgsConstructor
public class CreateContainer extends HttpClientFedora {

    private String path;
    private String name;

    private final String URL = FedoraConstant.FEDORA_URL;

    public CreateContainer(CreateContainerReqDTO dto) {
        this.path = dto.getPath();
        this.name = dto.getName();
    }

    @SneakyThrows
    @Override
    public String getResponse() {
        String token = new FedoraToken().getToken();

        HttpPost request = new HttpPost(URL + path);
        request.addHeader("Authorization", token);
        request.addHeader("Slug", name);

        HttpResponse response = builder().execute(request);
        return String.valueOf(response.getHeaders("Location")[0]);
    }
}

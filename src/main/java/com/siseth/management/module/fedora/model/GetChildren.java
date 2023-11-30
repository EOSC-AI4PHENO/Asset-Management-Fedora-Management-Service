package com.siseth.management.module.fedora.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siseth.management.constant.AppConstant;
import com.siseth.management.module.fedora.constant.FedoraConstant;
import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import com.siseth.management.module.internal.api.FedoraFilesDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetChildren extends HttpClientFedora {

    private String path;
    private Integer offset;
    private Integer max_results;


    private final String URL = FedoraConstant.FEDORA_URL;

    @SneakyThrows
    @Override
    public FedoraFilesDTO getResponse() {
        String token = new FedoraToken().getToken();
        HttpGet request = new HttpGet(AppConstant.FEDORA_URL + "fcr:search?condition=fedora_id%3Dinfo%3Afedora%2F"+path.replace("/","%2F")+"&offset="+offset+"&max_results="+max_results);
        request.addHeader("Authorization", token);
        HttpResponse response = builder().execute(request);
        FedoraFilesDTO files = new ObjectMapper().readValue(decode(response.getEntity()), FedoraFilesDTO.class);

        files.getItems().forEach(x -> x.setFedora_id(Arrays.stream(x.getFedora_id().split("/")).skip(5).collect(Collectors.joining("/"))));

        return files;
    }
}

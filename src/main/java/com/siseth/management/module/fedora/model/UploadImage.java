package com.siseth.management.module.fedora.model;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.module.fedora.constant.FedoraConstant;
import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@AllArgsConstructor
public class UploadImage extends HttpClientFedora {


    private String path;
    private String name;
    private MultipartFile multipartFile;
    private String realm;

    private final String URL = FedoraConstant.FEDORA_URL;

    public UploadImage(D_File dFile, MultipartFile file, String realm) {
        this.path = dFile.getDirectory();
        this.name = dFile.getName();
        this.multipartFile = file;
        this.realm = realm;
    }

    @SneakyThrows
    @Override
    public String getResponse() {
        String token = new FedoraToken().getToken();
        File file = File.createTempFile("Test", ".jpg");
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        file.deleteOnExit();

        HttpPost request = new HttpPost(URL + this.realm + "/" + path);
        request.setEntity(new FileEntity(file));
        request.addHeader("Authorization", token);
        request.addHeader("Slug", name);
        request.addHeader("Content-Type", "image/jpeg");
        HttpResponse response = builder().execute(request);
        checkStatus(response);
        return String.valueOf(response.getHeaders("Location")[0]);
    }
}

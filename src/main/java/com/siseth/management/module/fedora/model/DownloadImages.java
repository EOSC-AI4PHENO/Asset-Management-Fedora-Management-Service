package com.siseth.management.module.fedora.model;

import com.siseth.management.module.fedora.model.common.FedoraToken;
import com.siseth.management.module.fedora.model.common.HttpClientFedora;
import com.siseth.management.module.internal.api.ItemsDTO;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class DownloadImages extends HttpClientFedora {

    private List<ItemsDTO> paths;

    @SneakyThrows
    @Override
    public File getResponse() {
        if (new File("temp").exists()) {
            Files.walk(Paths.get("temp"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        String token = new FedoraToken().getToken();
        HttpClient client = builder();
        HttpGet request = new HttpGet();
        request.addHeader("Authorization", token);

        for (int i = 0; i < paths.size(); i++) {
            request.setURI(URI.create(paths.get(i).getFedora_id()));
            HttpResponse response = client.execute(request);
            byte[] byteArray = IOUtils.toByteArray(response.getEntity().getContent());
            FileUtils.writeByteArrayToFile(new File("temp/images/" + paths.get(i).getFedora_id().substring(paths.get(i).getFedora_id().lastIndexOf("/") + 1)), byteArray);
        }
        Path path = pack("temp/images", "temp/download.zip");
        return path.toFile();
    }

    @SneakyThrows
    public static Path pack(String sourceDirPath, String zipFilePath) {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
        }
        return p;
    }
}

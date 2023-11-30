package com.siseth.management.component.zip;

import com.siseth.management.component.entity.D_File;
import com.siseth.management.module.fedora.model.GetImage;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtil {

    private final String temporaryString = System.getProperty("java.io.tmpdir");

    private Set<String> names = new HashSet<>();

    @SneakyThrows
    public File zipFiles(List<D_File> fileList) {

        File zip = File.createTempFile("temp-file", ".zip");
        zip.deleteOnExit();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));

        for (D_File file : fileList) {
            byte[] bytes = new GetImage(file.getPath()).getResponse();

            File outputFile = new File(this.temporaryString, generateName(file.getName()));
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(bytes);
                ZipEntry zipEntry = new ZipEntry(outputFile.getName());
                out.putNextEntry(zipEntry);
                out.write(bytes);
                out.closeEntry();
            }
        }
        out.close();

        return zip;
    }

    private String generateName(String fileName) {
        String name = fileName;
        int i = 1;
        while(this.names.contains(name)) {
            int lastDot = name.lastIndexOf(".");
            name = name.substring(0, lastDot) + "_" + (i++) + "." + name.substring(lastDot + 1);

        }
        this.names.add(name);
        return name;
    }


}

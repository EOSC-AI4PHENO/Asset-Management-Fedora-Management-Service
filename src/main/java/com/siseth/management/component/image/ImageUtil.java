package com.siseth.management.component.image;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {

    public static String escapeString(String name) {
        return name.replaceAll("\\[", "%5B")
                                .replaceAll("\\]","%5D")
                                .replaceAll(" ", "%20");
    }

    public static String getBaseWithConvert(byte[] fileData, int width, int height) {
        return ImageUtil.convert( ImageUtil.scale(fileData, width, height) );
    }

    public static String convert(byte[] bytes) {
        return bytes != null ?
                Base64.getEncoder().encodeToString(bytes) :
                null;
    }

    @SneakyThrows
    public static boolean checkCorrect(MultipartFile file) {
        return file != null && ImageIO.read(new ByteArrayInputStream(file.getBytes())) != null;
    }

    public static byte[] scale(byte[] fileData, int width, int height) {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);

            if(height == 0) {
                height = (width * img.getHeight())/ img.getWidth();
            }
            if(width == 0) {
                width = (height * img.getWidth())/ img.getHeight();
            }

            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


}

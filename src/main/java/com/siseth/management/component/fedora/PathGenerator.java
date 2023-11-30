package com.siseth.management.component.fedora;

public final class PathGenerator {



    public static String generate(String type, String directory, Long sourceId, Long analysisDetailId) {
        try {
            if (directory.equals("")) directory = "default";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        switch (type) {
            case "image" :
                return "/source/" + sourceId + (directory.startsWith("/") ? "" :  "/") + (directory.endsWith("/") ? directory : directory + "/");
            case "json_roi" :
                return "/source/" + sourceId + "/roi/";
            case "json_result" :
                return "/analysis/"+
                        (analysisDetailId != null ? analysisDetailId + "/" : "")
                        + "roi/";
        }
        return "";
    }



}

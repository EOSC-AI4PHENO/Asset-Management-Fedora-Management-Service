package com.siseth.management.component.image;

import java.util.Map;

public class ImageSize {


    public static Map<String, Integer> enumToSize =
            Map.of("SMALL", 50  ,
                    "MEDIUM", 200,
                    "LARGE", 500);


    public static Integer getSize(String name){
        return name == null ?
                50 :
                enumToSize.getOrDefault(name, 50);
    }

    public static Integer getSmallSize(){
        return enumToSize.getOrDefault("SMALL", 50);
    }


}

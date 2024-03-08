package com.mymerit.mymerit.infrastructure.utils;

public class JudgeUtils {

    public static int getLanguageNumber(String filename){

        int lastFullStopIndex = filename.lastIndexOf(".");

        String lang = filename.substring(lastFullStopIndex + 1);

        switch(lang){
            case "c" :
                return 75;
            case "cpp" :
                return 54;
            case "java":
                return 62;
            default:
                return 43;

    }

    }
}

// c , c++ , csharp. java, pythom,
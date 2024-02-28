package com.mymerit.mymerit.infrastructure.utils;

public class JudgeUtils {

    public int getLanguageNumber(String filename){
        int langNumber;
        int lastFullStopIndex = filename.lastIndexOf(".");

        String lang = filename.substring(lastFullStopIndex + 1);

        switch(lang){
            case "c" :
                return 75;
            default:
                return 0;

    }

    }
}

// c , c++ , csharp. java, pythom,
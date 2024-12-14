package com.notmarra.notcredits.util;

public class LangFiles {
    public static void createLang() {
        for (String lang : SUPPORTED_LANGUAGES) {
            Files.createFile("lang/" + lang + ".yml");
        }
    }
}

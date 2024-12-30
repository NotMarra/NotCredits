package com.notmarra.notcredits.util;

import static com.notmarra.notcredits.NotCredits.SUPPORTED_LANGUAGES;

public class LangFiles {
    public static void createLang() {
        for (String lang : SUPPORTED_LANGUAGES) {
            Files.createFile("lang/" + lang + ".yml");
        }
    }
}

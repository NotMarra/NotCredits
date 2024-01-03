package com.notmarra.notcredits.utilities;

public class Lang {
        public static void createLang() {
            Files.createFolder("lang");
            Files.createFile("lang/en.yml");
            Files.createFile("lang/cz.yml");
        }

}

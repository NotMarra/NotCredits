package com.notmarra.notcredits.util;

import com.notmarra.notcredits.Notcredits;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class LangFiles {

    public static void createLang() {
        Files.createFile("lang/en.yml");
    }
}

package ru.erik182.miropolis.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения атрибутов файла конфигурации
 */

public class PropertyReader {
    private static final String PROP_FILE_URL = "src/test/resources/test.properties";
    private static InputStream configInputStream;
    private static Properties properties;

    static {
        try {
            configInputStream = new FileInputStream(PROP_FILE_URL);
            properties = new Properties();
            properties.load(configInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (configInputStream != null)
                try {
                    configInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * метод получения конкретного атрибута по ключу
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

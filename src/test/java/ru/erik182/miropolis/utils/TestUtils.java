package ru.erik182.miropolis.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebElement;


public class TestUtils {

    //метод для проверки соответсвия html-класса элементу
    public static boolean hasHtmlClass(WebElement element, String className) {
        String classes = element.getAttribute("class");
        for (String c : classes.split(" ")) {
            if (c.equals(className)) {
                return true;
            }
        }
        return false;
    }

    //метод генерации рандомной строки по количеству символов
    public static String generateRandomString(int length) {
        return RandomStringUtils.randomAscii(length);
    }
}

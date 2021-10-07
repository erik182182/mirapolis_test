package ru.erik182.miropolis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.erik182.miropolis.pages.AuthorizationPage;
import ru.erik182.miropolis.pages.ForgetPasswordPage;
import ru.erik182.miropolis.pages.ProfilePage;
import ru.erik182.miropolis.utils.PropertyReader;
import ru.erik182.miropolis.utils.TestUtils;

import java.util.concurrent.TimeUnit;

public class AuthorizationPageTest {

    private static WebDriver driver;
    private static AuthorizationPage authorizationPage;
    private static ProfilePage profilePage;
    private static ForgetPasswordPage forgetPasswordPage;

    /**
     * осуществление первоначальной настройки
     */
    @Before
    public void setup() {
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver", PropertyReader.getProperty("chrome.driver"));
        //создание экземпляра драйвера
        driver = new ChromeDriver();
        //создание объектов страниц
        authorizationPage = new AuthorizationPage(driver);
        profilePage = new ProfilePage(driver);
        forgetPasswordPage = new ForgetPasswordPage(driver);
        //развернуть окно на весь экран
        driver.manage().window().maximize();
        //установить задержку для получения элемента
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(PropertyReader.getProperty("chrome.driver.timeout")), TimeUnit.SECONDS);
        //открыть страницу
        driver.get(PropertyReader.getProperty("miropolis.auth.url"));
    }

    //тестирование авторизации: верный логин и пароль
    @Test
    public void authorizationSuccessfulLoginAndPasswordTest() {
        authorizationPage.inputLogin(PropertyReader.getProperty("miropolis.user.login"));
        authorizationPage.inputPassword(PropertyReader.getProperty("miropolis.user.password"));
        authorizationPage.clickAuthButton();
        //ожидание загрузки нужной страницы
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleIs(PropertyReader.getProperty("miropolis.profile.page.title")));
        //проверка данных на странице
        Assert.assertEquals(PropertyReader.getProperty("miropolis.profile.page.title"), profilePage.getTitle());
        Assert.assertEquals(PropertyReader.getProperty("miropolis.user.name"), profilePage.getUserName());
        profilePage.logout();
    }

    //тестирование авторизации: неверный логин и верный пароль
    @Test
    public void authorizationWrongLoginRightPasswordTest() {
        String login = PropertyReader.getProperty("miropolis.user.wrong.login");
        String password = PropertyReader.getProperty("miropolis.user.password");
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.wrong");
        authorizationWrongData(login, password, expectedMessage);
    }

    //тестирование авторизации: верный логин и неверный пароль
    @Test
    public void authorizationRightLoginWrongPasswordTest() {
        String login = PropertyReader.getProperty("miropolis.user.login");
        String password = PropertyReader.getProperty("miropolis.user.wrong.password");
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.wrong");
        authorizationWrongData(login, password, expectedMessage);
    }

    //тестирование авторизации: неверный логин и неверный пароль
    @Test
    public void authorizationWrongLoginWrongPasswordTest() {
        String login = PropertyReader.getProperty("miropolis.user.wrong.login");
        String password = PropertyReader.getProperty("miropolis.user.wrong.password");
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.wrong");
        authorizationWrongData(login, password, expectedMessage);
    }


    // тест падает, потому что найден небольшой косметический баг при авторизации:
    // при вводе пустых значений логина и пароля надпись в алерте "Неверные данные для авторизации" написана с точкой,
    // в остальных случаях (при вводе неверных данных) эта же надпись написана без точки.
    //TODO: исправить?
    @Test
    public void authorizationEmptyDataTest() {
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.wrong");
        authorizationWrongData("", "", expectedMessage);
    }

    //тестирование на ввод длинных строк
    //не указаны максимальные и минимальные значения длины строк для логина и пароля. Какие необходимо брать и тестировать?
    //TODO: Посмотреть в ТЗ
    @Test
    public void authorizationTooLongDataTest() {
        String login = TestUtils.generateRandomString(1024);
        String password = TestUtils.generateRandomString(1024);
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.long");
        authorizationWrongData(login, password, expectedMessage);
    }

    //Тестирование на SQL-инъекцию проверить невозможно, так как отсутствует доступ к базе данных для просмотра
    //TODO: проверить
    @Test
    public void authorizationSQLInjectionDataTest() {
        String login = PropertyReader.getProperty("miropolis.user.wrong.sql.injection");
        String password = PropertyReader.getProperty("miropolis.user.wrong.password");
        String expectedMessage = PropertyReader.getProperty("miropolis.auth.page.alert.wrong");
        authorizationWrongData(login, password, expectedMessage);
    }

    //общий метод для тестирования ввода неверных данных. Логика для всех проверок одинакова, и вынесена в этот метод
    private void authorizationWrongData(String login, String password, String expectedMessage) {
        authorizationPage.inputLogin(login);
        authorizationPage.inputPassword(password);
        authorizationPage.clickAuthButton();
        //получить текст из алерта
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(expectedMessage, alert.getText());
        alert.accept();
        Assert.assertTrue(authorizationPage.getLogin().isEmpty());
        Assert.assertTrue(authorizationPage.getPassword().isEmpty());
        TestUtils.hasHtmlClass(authorizationPage.getAuthForm(), PropertyReader.getProperty("miropolis.html.class.invalid"));
    }

    //проверка кнопки просмотра пароля
    @Test
    public void showPasswordButtonTest() {
        authorizationPage.inputPassword(PropertyReader.getProperty("miropolis.user.wrong.password"));
        Assert.assertEquals("password", authorizationPage.getPasswordField().getAttribute("type"));
        authorizationPage.clickShowPasswordButton();
        Assert.assertEquals("text", authorizationPage.getPasswordField().getAttribute("type"));
    }

    //проверка ссылок
    @Test
    public void forgetPasswordButtonBackLinkTest() {
        authorizationPage.clickForgetPasswordLink();
        Assert.assertEquals(driver.getTitle(), PropertyReader.getProperty("miropolis.forget.page.title"));
        forgetPasswordPage.clickBackLink();
        Assert.assertEquals(driver.getTitle(), PropertyReader.getProperty("miropolis.auth.page.title"));
    }

    //проверка восстановаления пароля при верно введенном логине
    //проверка восстановления невозможна, так как нет доступа к аккаунту
    //TODO: проверить восстановление пароля
    @Test
    public void forgetPasswordRightLoginTest() {
        String login = PropertyReader.getProperty("miropolis.user.login");
        authorizationPage.clickForgetPasswordLink();
        forgetPasswordPage.inputLogin(login);
        forgetPasswordPage.clickSubmitButton();
        Assert.assertTrue(TestUtils.hasHtmlClass(forgetPasswordPage.getMessageDiv(), PropertyReader.getProperty("miropolis.html.class.success")));
    }

    //проверка сообщения о некорректном логине при попытке восстановить пароль
    @Test
    public void forgetPasswordWrongLoginTest() {
        String login = PropertyReader.getProperty("miropolis.user.wrong.login");
        authorizationPage.clickForgetPasswordLink();
        forgetPasswordPage.inputLogin(login);
        forgetPasswordPage.clickSubmitButton();
        Assert.assertTrue(TestUtils.hasHtmlClass(forgetPasswordPage.getMessageDiv(), PropertyReader.getProperty("miropolis.html.class.alert")));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

}

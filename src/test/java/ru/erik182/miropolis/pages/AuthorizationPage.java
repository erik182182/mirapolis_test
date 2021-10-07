package ru.erik182.miropolis.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

// объект страницы авторизации
public class AuthorizationPage {

    @FindBy(xpath = "//input[contains(@class, 'mira-default-login-page-text-input') and @placeholder = 'Введите ваш логин']")
    private WebElement loginField;

    @FindBy(xpath = "//input[contains(@class, 'mira-default-login-page-text-input') and @placeholder = 'Введите ваш пароль']")
    private WebElement passwordField;

    @FindBy(xpath = "//*[@id = 'button_submit_login_form']")
    private WebElement authBtn;

    @FindBy(xpath = "//*[@id = 'show_password']")
    private WebElement showPasswordBtn;

    @FindBy(xpath = "//form[contains(@class, 'mira-login-form-remember')]")
    private WebElement authForm;

    @FindBy(xpath = "//a[@class = 'mira-default-login-page-link']")
    private WebElement forgetPasswordLink;

    private WebDriver driver;

    public AuthorizationPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void inputLogin(String login) {
        loginField.sendKeys(login);
    }

    public String getLogin() {
        return loginField.getText();
    }

    public void inputPassword(String password) {
        passwordField.sendKeys(password);
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void clickAuthButton() {
        authBtn.click();
    }

    public void clickShowPasswordButton() {
        showPasswordBtn.click();
    }

    public WebElement getAuthForm() {
        return authForm;
    }

    public WebElement getPasswordField() {
        return passwordField;
    }

    public void clickForgetPasswordLink() {
        forgetPasswordLink.click();
    }

}

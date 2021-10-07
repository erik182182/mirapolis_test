package ru.erik182.miropolis.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

//объект страницы восстановления пароля
public class ForgetPasswordPage {

    @FindBy(xpath = "//input[contains(@class, 'mira-widget-login-input')]")
    private WebElement loginField;

    @FindBy(xpath = "//button[@type = 'submit']")
    private WebElement submitBtn;

    @FindBy(xpath = "//a[@class = 'mira-page-forgot-password-link']")
    private WebElement backLink;

    @FindBy(xpath = "//form/following::div")
    private WebElement messageDiv;

    private WebDriver driver;

    public ForgetPasswordPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void inputLogin(String login) {
        loginField.sendKeys(login);
    }

    public void clickSubmitButton() {
        submitBtn.click();
    }

    public void clickBackLink() {
        backLink.click();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public WebElement getMessageDiv() {
        return messageDiv;
    }
}

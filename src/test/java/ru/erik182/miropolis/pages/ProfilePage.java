package ru.erik182.miropolis.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

//объект страницы профиля
public class ProfilePage {

    @FindBy(xpath = "//div[contains(@class, 'avatar-full-name')]")
    private WebElement userName;

    @FindBy(xpath = "//*[@data-mira-id = 'Template29']")
    private WebElement userDropDown;

    @FindBy(xpath = "//*[@id=\"DropDownContainer28-DD\"]//span[text() = 'Выйти']")
    private WebElement logoutBtn;

    private WebDriver driver;

    public ProfilePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getUserName() {
        return userName.getText();
    }

    public void clickUserDropDown() {
        userDropDown.click();
    }

    public void clickLogoutButton() {
        logoutBtn.click();
    }

    public void logout() {
        clickUserDropDown();
        clickLogoutButton();
    }

}

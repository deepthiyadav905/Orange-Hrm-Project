package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	public ActionDriver actiondriver;

	// constructor
	public HomePage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}

	// defining the locators..
	private By adminTab = By.xpath("//span[normalize-space()='Admin']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']");
	private By userIdTab = By.xpath("//p[@class='oxd-userdropdown-name']");
	private By logoutButton = By.className("oxd-userdropdown-link");

	// Method to verify is admin is visible
	public Boolean isAdminVisible() {
		actiondriver.applyBorder(adminTab, "green");
		return actiondriver.isDisplayed(adminTab);
	}
	// Method to verify is orangeHRM is visible

	public Boolean isOrangeHRMLogoVisible() {
		actiondriver.applyBorder(orangeHRMLogo, "red");
		return actiondriver.isDisplayed(orangeHRMLogo);
	}

	// method to logout

	public void logOut() {
		actiondriver.click(userIdTab);
		actiondriver.applyBorder(userIdTab, "red");
		actiondriver.applyBorder(logoutButton, "red");
		actiondriver.click(logoutButton);

	}

}

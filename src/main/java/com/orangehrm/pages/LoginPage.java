package com.orangehrm.pages;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	ActionDriver actiondriver;
	public static final Logger logger = BaseClass.logger;

	// constructor
	public LoginPage(WebDriver driver) {
		this.actiondriver = BaseClass.getActionDriver();
	}

	By username = By.name("username");
	By password = By.cssSelector("input[type='password']");
	By loginbtn = By.cssSelector("button.orangehrm-login-button");
	By errmsg = By.xpath("//p[text()='Invalid credentials']");

	// method to perform login
	public void login(String Username, String Password) {
		actiondriver.elementToBeVisible(username);
		logger.info("Entering username"); // debug log
		actiondriver.enterText(username, Username);
		logger.info("Entering password");// debug log
		actiondriver.enterText(password, Password);
		logger.info("Clicking login button");// debug log
		actiondriver.click(loginbtn);
		logger.info("Successfully logined to dashboard");

	}

	// method to check if error message displayed

	public boolean isErrorMessageDisplayed() {
		actiondriver.applyBorder(errmsg, "green");
		return actiondriver.isDisplayed(errmsg);
	}

	// method to get text from the error message
	public String getErrortext() {

		return actiondriver.getText(errmsg);
	}

	// verify error message displayed correct or not

	public boolean toVerifyErrorMessage() {
		return actiondriver.compareText(errmsg, "Invalid credentials");

	}

}

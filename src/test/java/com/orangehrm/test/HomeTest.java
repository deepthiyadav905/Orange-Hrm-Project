package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

@Listeners(com.orangehrm.listeners.TestListner.class)

public class HomeTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	/*
	 * @Test public void verifyOrangeHRMLogo() { loginpage.login("Admin",
	 * "admin123"); Assert.assertTrue(homepage.isOrangeHRMLogoVisible(),
	 * "Test failed, logo not displayed"); }
	 */

	@Test
	public void verifyOrangeHRMLogo() {

		// 🔥 START TEST
		// ExtentManager.startTest("Verify OrangeHRM Logo Test"); //--> this has been
		// implemented in testng listner

		ExtentManager.logInfo("Starting logo verification test");

		loginpage.login("Admin", "admin123");
		ExtentManager.logInfo("Logged in successfully");

		boolean logoVisible = homepage.isOrangeHRMLogoVisible();

		if (logoVisible) {
			ExtentManager.logPass("OrangeHRM logo is displayed");
		} else {
			ExtentManager.logFail("OrangeHRM logo not displayed");

			try {
				ExtentManager.attachScreenshot(getDriver(), "Logo Failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Assert.assertTrue(logoVisible, "Test failed, logo not displayed");
	}

}

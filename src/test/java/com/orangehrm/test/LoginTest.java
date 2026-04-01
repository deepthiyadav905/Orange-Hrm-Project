package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

@Listeners(com.orangehrm.listeners.TestListner.class)
public class LoginTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod

	public void setupPages() {
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	/*
	 * public void verifyLogin() throws InterruptedException {
	 * loginpage.login("Admin", "admin123");
	 * Assert.assertTrue(homepage.isAdminVisible()); Thread.sleep(1000); //
	 * temporary stabilization this is added to pass the parallel testcases..
	 * homepage.logOut(); }
	 */

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)

	// as we are implemented below one in at listeners level
	// @Test(dataProvider = "validLoginData", dataProviderClass =
	// DataProviders.class, retryAnalyzer =
	// com.orangehrm.utilities.RetryAnalyzer.class)

	// after extent manager came we modified the code like below..

	public void verifyLogin(String username, String password) throws InterruptedException {

		// 🔥 START TEST
		// ExtentManager.startTest("Verify Login Test"); //--> this has been implemented
		// in testng listner

		ExtentManager.logInfo("Starting login test");

		loginpage.login(username, password);
		ExtentManager.logInfo("Entered valid credentials");

		boolean isAdmin = homepage.isAdminVisible();

		if (isAdmin) {
			ExtentManager.logPass("Login successful - Admin visible");
		} else {
			ExtentManager.logFail("Login failed");

			try {
				ExtentManager.attachScreenshotOnFail(getDriver(), "Login Failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Assert.assertTrue(isAdmin);

		Thread.sleep(1000);
		homepage.logOut();
		ExtentManager.logInfo("Logged out successfully");
	}

	/*
	 * public void invalidLoginTest() { loginpage.login("deepthi", "admin");
	 * Assert.assertTrue(loginpage.toVerifyErrorMessage(),
	 * "Test failed, Invalid Error message"); }
	 */

	@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class)

	public void invalidLoginTest(String username, String password) {

		// 🔥 START TEST
		// ExtentManager.startTest("Invalid Login Test"); //--> this has been
		// implemented in testng listner

		ExtentManager.logInfo("Starting invalid login test");

		loginpage.login(username, password);

		boolean error = loginpage.toVerifyErrorMessage();

		if (error) {
			ExtentManager.logPass("Error message displayed correctly");
		} else {
			ExtentManager.logFail("Error message not displayed");

			try {
				ExtentManager.attachScreenshot(getDriver(), "Invalid Login Failure");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Assert.assertTrue(error, "Test failed, Invalid Error message");
	}

}

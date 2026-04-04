package com.orangehrm.test;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

@Listeners(com.orangehrm.listeners.TestListner.class)

public class DummyTest extends BaseClass {

	/*
	 * @Test public void dummytest() { String title = getDriver().getTitle(); assert
	 * title.equals("OrangeHRM") :
	 * "Test failed - Title is not matching with the actual title";
	 * System.out.println("The title is matching"); }
	 */

	@Test
	public void dummytest() {

		// 🔥 START TEST
		// ExtentManager.startTest("Dummy Test"); --> this has been implemented in
		// testng listner
		// ok - testing

		String title = getDriver().getTitle();

		ExtentManager.logInfo("Fetched page title");

		if (title.equals("OrangeHRM")) {
			ExtentManager.logPass("Title matched successfully");
			logger.info("The title is matching");
		} else {
			ExtentManager.logFail("Title not matching");

			try {
				ExtentManager.attachScreenshot(getDriver(), "Title Mismatch");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Assertion
		assert title.equals("OrangeHRM") : "Test failed - Title is not matching";
	}

}

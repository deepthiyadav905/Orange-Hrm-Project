package com.orangehrm.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;

public class BaseClass {

	// protected static WebDriver driver;
	protected static Properties prop;
	// private static ActionDriver actionDriver;
	public static final Logger logger = LogManager.getLogger(BaseClass.class);

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	@BeforeSuite
	public void loadConfigureFile() throws IOException {
		// here we need to create the object for properties fils to get the key value
		// pairs from that file..
		prop = new Properties();
		// FileInputStream fis = new
		// FileInputStream("src/main/resources/config.properties"); we can write in this
		// way
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
		prop.load(is);
		logger.info("properties file loaded");
	}

	@BeforeMethod
	public void setup() {
		launchBrowser();
		browserConfigure();
		logger.info("driver intialized and driver maximised");
		logger.trace("trace message");
		logger.error("error message");
		logger.debug("debug message");

		logger.fatal("fatal message");

		logger.warn("warn message");

		// ✅ Initialize ActionDriver ONLY ONCE

		if (actionDriver.get() == null) {
			actionDriver.set(new ActionDriver(getDriver()));
			System.out.println("ActionDriver instance created.  ThreadID :" + Thread.currentThread().getId());

		}

	}

	// intialise the webdriverbased on browser defined in config file
	private void launchBrowser() {
		String browser = prop.getProperty("browser");
		if (browser.contains("chrome")) {
			// driver = new ChromeDriver();
			driver.set(new ChromeDriver()); // Replacing above with below due to thread local concept
			logger.info(browser + " driver initialized");
		} else if (browser.contains("edge")) {
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver()); // Replacing above with below due to thread local concept

			logger.info(browser + " driver initialized");
		} else if (browser.contains("firefox")) {
			// driver = new FirefoxDriver();
			driver.set(new FirefoxDriver()); // //Replacing above with below due to thread local concept
			logger.info(browser + " driver initialized");
		} else {
			throw new IllegalArgumentException("browser not supported:" + browser);
		}
	}

	// configure browser for implicit and to maximize
	private void browserConfigure() {
		// Navigate to URL
		String URL = prop.getProperty("URL");
		try {
			// driver.get(URL);
			getDriver().get(URL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("failed to Navigate the url" + e.getMessage());
		}
		// maximize the browser
		driver.get().manage().window().maximize();// Replacing above with below due to thread local concept
		getDriver().manage().window().maximize();

	}

	// ✅ Getter for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			throw new IllegalStateException("ActionDriver not initialized");
		}
		return actionDriver.get();
	}

	// getdriver method
	public static WebDriver getDriver() {
		return driver.get();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		Thread.sleep(2000); // allow listener to finish

		if (getDriver() != null) {
			getDriver().quit();
			logger.info("Driver closed");
		}

		/*
		 * // ✅ Reset Singleton driver = null; actionDriver = null;
		 */

		driver.remove();
		actionDriver.remove();
	}
}
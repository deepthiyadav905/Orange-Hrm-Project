package com.orangehrm.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	// 🔥 Create Report
	public static ExtentReports getReporter() {

		if (extent == null) {

			String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Report");
			spark.config().setDocumentTitle("Test Execution Report");

			extent = new ExtentReports();
			extent.attachReporter(spark);

			extent.setSystemInfo("OS", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User", System.getProperty("user.name"));
		}

		return extent;
	}

	// 🔥 Start Test
	public static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// 🔥 Get Current Test
	public static ExtentTest getTest() {
		if (test.get() == null) {
			throw new RuntimeException("ExtentTest is null. Did you call startTest()?");
		}
		return test.get();
	}

	// 🔥 Log Info
	public static void logInfo(String message) {
		getTest().info(message);
	}

	// 🔥 Log Pass
	public static void logPass(String message) {
		getTest().pass(message);
	}

	// Log Fail
	public static void logFail(String message) {
		getTest().fail(message);
	}

	// log skip
	public static void logSkip(String message) {
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", message);
		getTest().skip(colorMessage);
	}

	// 🔥 Take Screenshot
	public static String takeScreenshot(WebDriver driver, String screenshotName) {

		if (driver == null) {
			System.out.println("Driver is null, screenshot skipped");
			return "";
		}

		String path = "";

		try {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File src = ts.getScreenshotAs(OutputType.FILE);

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

			path = System.getProperty("user.dir") + "/reports/screenshots/" + screenshotName + "_" + timeStamp + ".png";

			File dest = new File(path);
			FileUtils.copyFile(src, dest);

		} catch (Exception e) {
			System.out.println("Screenshot error: " + e.getMessage());
		}

		return path;
	}

	/*
	 * As we are using separate methods for each and every log so no use of
	 * this..... // 🔥 Attach Screenshot to Report public static void
	 * attachScreenshot(WebDriver driver, String message) { String screenshotPath =
	 * takeScreenshot(driver, "Screenshot"); getTest().fail(message,
	 * MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()); }
	 */

	// genereal way to attach screensgot
	public static void attachScreenshot(WebDriver driver, String message) {
		String path = takeScreenshot(driver, "Screenshot");
		getTest().info(message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
	}

	// Fail Screenshot with the message
	public static void attachScreenshotOnFail(WebDriver driver, String message) {
		try {
			String path = takeScreenshot(driver, "Failure");

			if (!path.isEmpty()) {
				getTest().fail(message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} else {
				getTest().fail(message);
			}

		} catch (Exception e) {
			getTest().fail("Screenshot failed: " + e.getMessage());
		}
	}

	// Pass screenshot with pass message
	public static void attachScreenshotOnPass(WebDriver driver, String message) {
		try {
			String path = takeScreenshot(driver, "Success");

			if (!path.isEmpty()) {
				getTest().pass(message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} else {
				getTest().pass(message);
			}

		} catch (Exception e) {
			getTest().pass("Screenshot failed but test passed");
		}
	}

	public static void unload() {
		test.remove();
	}

	// 🔥 Flush Report
	public static void flushReport() {
		getReporter().flush();

	}
}
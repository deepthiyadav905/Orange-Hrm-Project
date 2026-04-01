package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListner implements ITestListener, IAnnotationTransformer {

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

		// Attach RetryAnalyzer to all test methods
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		ExtentManager.startTest(testName);
		ExtentManager.logInfo("Test Started: " + testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		// ✅ VERY IMPORTANT
		ExtentManager.getTest().pass("Test Passed");

		try {
			ExtentManager.attachScreenshotOnPass(BaseClass.getDriver(), testName + " Passed");
		} catch (Exception e) {
			System.out.println("Screenshot failed: " + e.getMessage());
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		// ✅ Log failure properly
		ExtentManager.getTest().fail(result.getThrowable());

		try {
			ExtentManager.attachScreenshotOnFail(BaseClass.getDriver(), testName + " Failed");
		} catch (Exception e) {
			System.out.println("Screenshot failed: " + e.getMessage());
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		ExtentManager.getTest().skip("Test Skipped: " + testName);
	}

	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter(); // ensure initialized
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.flushReport(); // VERY IMPORTANT
	}
}
package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;

public class ActionDriver {

	WebDriver driver;
	WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	// constructor
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// wait for element to be clickable
	public void elementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("element is not clickable :" + e.getMessage());
		}
	}

	// wait for element to be visible
	public void elementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("element is not visible :" + e.getMessage());
		}
	}

	// click the element

	public void click(By by) {
		try {
			WebElement element = driver.findElement(by);
			elementToBeVisible(by);
			elementToBeClickable(by);
			element.click();
			logger.info("Clicked on element --> " + by.toString());

		} catch (Exception e) {
			logger.error("Unable to click element: " + by.toString());
			throw new RuntimeException(e);
		}
	}

	// method to enter the text in the input field
	public void enterText(By by, String value) {
		try {
			elementToBeVisible(by);

			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);

			logger.info("Entered text '" + value + "' into --> " + getElementDescription(by));

		} catch (Exception e) {
			logger.error("Unable to enter text into element: " + getElementDescription(by));
			throw e;
		}
	}
	// method to getText from input field

	public String getText(By by) {
		elementToBeVisible(by);
		try {
			return driver.findElement(by).getText();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("unable to get the text :" + e.getMessage());
			return null;
		}
	}

	// Method to two compare two text

	public boolean compareText(By by, String expectedText) {
		elementToBeVisible(by);
		String actualText;
		try {
			actualText = driver.findElement(by).getText();
			if (actualText.equals(expectedText)) {
				logger.info(actualText + "and" + expectedText + "both are same");
				return true;
			} else {
				logger.warn(actualText + "and" + expectedText + "both are not same");
				return false;
			}
		} catch (Exception e) {
			logger.error("unable to compare the text :" + e.getMessage());
		}
		return false;
	}

//method to check if an element isdisplayed

	public boolean isDisplayed(By by) {
		try {
			elementToBeVisible(by);

			boolean result = driver.findElement(by).isDisplayed();

			logger.info("Element is displayed --> " + getElementDescription(by));
			return result;

		} catch (Exception e) {
			logger.error("Element not displayed --> " + getElementDescription(by));
			return false;
		}
	}

	// wait for to pageload
	public void waitForPageLoad(int timeOutInSec) {

		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(webDriver -> ((JavascriptExecutor) webDriver)
					.executeScript("return document.readyState").equals("complete"));

			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds");
		}
	}

	// Scroll to element

	public void scrollToElement(By by) {

		WebElement element = driver.findElement(by);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public String getElementDescription(By locator) {

		elementToBeVisible(locator);

		try {
			if (driver == null)
				return "Driver is null";
			if (locator == null)
				return "Locator is null";

			WebElement element = driver.findElement(locator);

			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeholder: " + placeHolder;
			} else {
				return locator.toString();
			}

		} catch (Exception e) {
			logger.error("Unable to describe the element: " + e.getMessage());
			return locator.toString();
		}
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	private String truncate(String value, int maxLength) {
		if (value != null && value.length() > maxLength) {
			return value.substring(0, maxLength);
		}
		return value;
	}

	// Utility Method to Border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);

			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);

			logger.info("Applied the border with color " + color + " to element: " + getElementDescription(by));

		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by), e);
		}
	}
}

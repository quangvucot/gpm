package com.vdqgpm.webdriver;

import org.openqa.selenium.WebDriver;

public class WebDriverFactory {
	private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

	public static WebDriver getDriver() {
		return driverThreadLocal.get();
	}

	public static void setDriver(WebDriver driver) {
		driverThreadLocal.set(driver);
	}

	public static void removeDriver() {
		driverThreadLocal.remove();
	}
}

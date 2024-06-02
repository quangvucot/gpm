package com.vdqgpm.webdriver;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Point;

public class WebDriverManager {
//	private List<WebDriver> webDrivers = new ArrayList<>();
	private static Map<String, WebDriver> drivers = new HashMap<>();

	public WebDriver initializeDrivers(File file, String remoteAddress, String width, String height, int x, int y) {
		ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort()
				.build();
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", remoteAddress);
		try {
			service.start();
			WebDriver driver = new ChromeDriver(service, options);
			if (x > 0 && y > 0) {
				driver.manage().window()
						.setSize(new org.openqa.selenium.Dimension(Integer.parseInt(width), Integer.parseInt(height)));
				driver.manage().window().setPosition(new Point(x, y));
			}

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			drivers.put(remoteAddress, driver);
			WebDriverFactory.setDriver(driver); // Set the driver to ThreadLocal

			return driver;
		} catch (IOException e) {
			System.out.println("Lỗi khi thực thi trên profile: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public void quitDriver() {
		WebDriver driver = WebDriverFactory.getDriver();
		if (driver != null) {
			driver.quit();
			WebDriverFactory.removeDriver();
		}
	}

	public void quitAllDrivers() {
		for (WebDriver driver : drivers.values()) {
			if (driver != null) {
				driver.quit();
			}
		}
		drivers.clear();
	}
}

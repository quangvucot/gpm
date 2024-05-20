package com.vdqgpm.webdriver;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {
//	private List<WebDriver> webDrivers = new ArrayList<>();
	private static Map<String, WebDriver> drivers = new HashMap<>();

	public WebDriver initializeDrivers(File file, String remoteAddress) {
		ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort()
				.build();
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("debuggerAddress", remoteAddress);
		try {
			service.start();
			WebDriver driver = new ChromeDriver(service, options);
			driver.manage().window().setSize(new Dimension(600, 500));
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			drivers.put(remoteAddress, driver);
			return driver;
		} catch (IOException e) {
			System.out.println("Lỗi khi thực thi trên profile: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// Đóng tất cả các WebDriver khi không cần thiết nữa
	public void quitDriver(WebDriver driver) {
		if (driver != null) {
			driver.quit();
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

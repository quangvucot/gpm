package com.vdqgpm.pages;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import com.vdqgpm.webdriver.WebDriverManager;

import java.util.concurrent.ExecutorService;

public class NurturePage {
	// private static final int MAX_THREADS = 10; // Số lượng profile tối đa có thể
	// mở cùng lúc
	private static final Logger logger = LogManager.getLogger(NurturePage.class);

	public void setUp(String remoteAddress, File file) throws InterruptedException {
		WebDriverManager webDriverManager = new WebDriverManager();
		WebDriver driver = webDriverManager.initializeDrivers(file, remoteAddress);
		if (driver != null) {
			try {
				driver.get("https://google.com/");
				SendGmail sendGmail = new SendGmail(driver, 10);
				sendGmail.moveToGmail();
				sendGmail.writeMail();
				sendGmail.enterReceiver("daoquangvu.it01@gmail.com");
				sendGmail.enterSubject("Thư mời đi tham gia sự kiện của công Ty BaclinkAZ");
				sendGmail.enterBody("Xin chào bạn!");
				// sendGmail.clickSendEmail();
			} catch (Exception e) {
				logger.error("Đang xảy ra lỗi tại địa chỉ: " + remoteAddress, e);
			} finally {
				driver.quit();
			}

		}

	}

}

package com.vdqgpm.pages;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.vdqgpm.webdriver.WebDriverManager;

public class NurturePage {
	// private static final int MAX_THREADS = 10; // Số lượng profile tối đa có thể
	// mở cùng lúc
	private static final Logger logger = LogManager.getLogger(NurturePage.class);

	public void setUp(String remoteAddress, File file, String width, String height, int x, int y, Boolean isSendEmail)
			throws InterruptedException {
		WebDriverManager webDriverManager = new WebDriverManager();
		WebDriver driver = webDriverManager.initializeDrivers(file, remoteAddress, width, height, x, y);
		if (driver != null) {
			try {
				driver.get("https://google.com/");
				if (!isSendEmail) {
					SendGmail sendGmail = new SendGmail(driver, 20);
					sendGmail.moveToGmail();
					sendGmail.writeMail();
					sendGmail.enterReceiver("daoquangvu.it01@gmail.com");
					sendGmail.enterSubject("Thư mời đi tham gia sự kiện của công Ty BaclinkAZ");
					sendGmail.enterBody("Xin chào bạn!");
//					sendGmail.clickSendEmail();
				}

				ReadGoogleNews readGgNews = new ReadGoogleNews(driver, 20);
				readGgNews.openNewTab();
				readGgNews.accessToGoogleFeed();
				readGgNews.scrollViewNews();
				readGgNews.readRandomPost();

				YoutubePage youtubePage = new YoutubePage(driver, 20);
				youtubePage.accessToYoutube();
				youtubePage.findChanelToWatch();
				youtubePage.chooseVideoToWatch();
			} catch (Exception e) {
				logger.error("Đang xảy ra lỗi tại địa chỉ: " + remoteAddress, e);
			} finally {
				driver.quit();
			}

		}

	}

	public void connectSingleProfile(String remoteAddress, File file) {
		WebDriverManager webDriverManager = new WebDriverManager();
		WebDriver driver = webDriverManager.initializeDrivers(file, remoteAddress, "", "", 0, 0);
		if (driver != null) {
			try {
			} catch (Exception e) {
				logger.error("Đang xảy ra lỗi tại địa chỉ: " + remoteAddress, e);
			}

		}
	}

}

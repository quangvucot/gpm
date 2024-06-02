package com.vdqgpm.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.vdqgpm.utilities.Utils;

public class ReadGoogleNews extends BasePage {

	private By appListOfGoogle = By.xpath("//a[@class='gb_d' or contains(@href, 'https://www.google.com.vn/intl/')]");
	private By frameListApp = By.xpath("//iframe[@role='presentation' and @name='app']");
	private By googleNewsIcon = By.xpath("//a[@class='tX9u1b' and contains(@href, 'https://news.google.com')]");
	private By topNewsFeed = By.xpath("//div[@class='KfaKvb AdrDwd']//div[@class='n3GXRc']//h3//a");
	private By postBy = By.xpath("//article[@class='IBr9hb']//a[@class='WwrzSb']");

	public ReadGoogleNews(WebDriver driver, long timeoutInSeconds) {
		super(driver, timeoutInSeconds);
	}

	public void openNewTab() {
		openNewTab("https://google.com/");
		switchToTab(1);
	}

	public void accessToGoogleFeed() {
		sleepSecond(4000);
		Boolean isAccountEnable = waitForElementVisible(appListOfGoogle, 10);
		System.out.println(isAccountEnable);
		if (isAccountEnable) {
			driver.findElement(appListOfGoogle).click();
			sleepSecond(2000);
			WebElement frameElement = driver.findElement(frameListApp);
			driver.switchTo().frame(frameElement);
			sleepSecond(2000);
			try {
				moveToElement(googleNewsIcon);
				sleepSecond(1000);
				clickElement(googleNewsIcon);
				sleepSecond(2000);
			} catch (Exception e) {
				driver.switchTo().defaultContent();
				sleepSecond(2000);
				driver.findElement(appListOfGoogle).click();
				WebElement frameElements = driver.findElement(frameListApp);
				driver.switchTo().frame(frameElements);
				sleepSecond(2000);
				moveToElement(googleNewsIcon);
				sleepSecond(1000);
				clickElement(googleNewsIcon);
				sleepSecond(2000);
				// TODO: handle exception
			}

		}
	}

	public void scrollViewNews() {
		driver.switchTo().defaultContent();
		smoothScrollTo(1000, 300, 50);
		sleepSecond(3000);
		smoothScrollToTop(200, 50);
		WebElement topNewsElement = driver.findElement(topNewsFeed);
		sleepSecond(2000);
		topNewsElement.click();
		sleepSecond(4000);
	}

	public void readRandomPost() {

		Boolean isPostElementVisibility = waitForElementVisible(postBy, 20);
		if (isPostElementVisibility) {
			List<WebElement> elements = driver.findElements(postBy);
			System.out.println("Số lượng element có trên trang: " + elements.size());
			int totalReadPost = Utils.randomNumber(2, 5);
			System.out.println("Số bài báo đọc: " + totalReadPost);

			for (int i = 0; i < totalReadPost; i++) {
				List<String> tabs = null;
				int postNumber = Utils.randomNumber(1, 5);
				WebElement secondElement = elements.get(postNumber);
				smoothScrollToElement(secondElement, 200, 50);
				sleepSecond(1000);
				secondElement.click();
				sleepSecond(2000);
				tabs = new ArrayList<>(driver.getWindowHandles());
				driver.switchTo().window(tabs.get(2));
				smoothScrollTo(2000, 400, 50);
				driver.close();
				sleepSecond(1000);
				tabs = new ArrayList<>(driver.getWindowHandles());
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
					sleepSecond(3000);
					if (driver.getCurrentUrl().contains("https://news.google.com/")) {
						System.out.println("Switched to new tab with URL: " + driver.getCurrentUrl());
						break;
					}
				}
				sleepSecond(3000);
			}
		}

	}

}

package com.vdqgpm.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.vdqgpm.utilities.Utils;

public class YoutubePage extends BasePage {
	private By appListOfGoogle = By.xpath("//a[@class='gb_d' or contains(@href, 'https://www.google.com.vn/intl/')]");
	private By frameListApp = By.xpath("//iframe[@role='presentation' and @name='app']");
	private By youtubeElement = By.xpath("//a[@class='tX9u1b' and contains(@href, 'https://www.youtube.com')]");
	private By searchYoutubeIcon = By.xpath(
			"//yt-icon-button[@id='search-button-narrow']//button[@id='button' and @class='style-scope yt-icon-button']");
	private By searchForm = By.xpath("//form[@id='search-form']");
	private By inputSearch = By.xpath("//input[@name='search_query']");
	private By singleVideo = By.xpath("//a[@class='yt-simple-endpoint style-scope ytd-video-renderer']");

	private By dialogTrialFree = By.xpath("//div[@class='style-scope yt-mealbar-promo-renderer']");
	private By buttonAction = By.xpath("//div[@id='top-level-buttons-computed']");
	private By btnLikeVideo = By.xpath(
			"//button[@class='yt-spec-button-shape-next' or @title='Tôi thích video này' or @title='I like this']");
	private By skip = By.xpath("//yt-button-renderer[@id='action-button']"); //

	public YoutubePage(WebDriver driver, long timeoutInSeconds) {
		super(driver, timeoutInSeconds);

	}

	public void accessToYoutube() {
		openNewTab("https://google.com/");
		sleepSecond(4000);
		Boolean isAccountEnable = waitForElementVisible(appListOfGoogle, 10);
		System.out.println(isAccountEnable);
		if (isAccountEnable) {
			sleepSecond(2000);
			driver.findElement(appListOfGoogle).click();
			sleepSecond(2000);
			WebElement frameElement = driver.findElement(frameListApp);
			driver.switchTo().frame(frameElement);
			sleepSecond(2000);
			moveToElement(youtubeElement);
			sleepSecond(1000);
			clickElement(youtubeElement);
			sleepSecond(2000);
			driver.switchTo().defaultContent();

		}
	}

	public void findChanelToWatch() {
		Boolean seachYoutubeIcon = waitForElementVisible(searchYoutubeIcon, 20);
		if (seachYoutubeIcon) {
			driver.findElement(searchYoutubeIcon).click();
			sleepSecond(3000);
			Boolean formInput = waitForElementVisible(searchForm, 20);
			if (formInput) {
				WebElement inputYoutubeSearch = driver.findElement(inputSearch);
				sendKeysSlowly(inputYoutubeSearch, "Web 5 ngay", 200);
				sleepSecond(2000);
				inputYoutubeSearch.sendKeys(Keys.ENTER);
				sleepSecond(4000);
			} else {

			}
		}
	}

	public void chooseVideoToWatch() {
		Boolean isSingleVideoVisibility = waitForElementVisible(singleVideo, 20);
		if (isSingleVideoVisibility) {
			sleepSecond(2000);
			List<WebElement> videoList = new ArrayList<>();
			videoList = driver.findElements(singleVideo);
			int randomVideo = Utils.randomNumber(0, 5);
			System.out.println("Xem video số: " + videoList.get(randomVideo).getText());
			sleepSecond(3000);
			smoothScrollToElement(videoList.get(randomVideo), 100, 20);
			sleepSecond(2000);
			clickElement(videoList.get(randomVideo));
			int timeToSleep = Utils.randomNumber(60000, 120000);
			sleepSecond(timeToSleep);
			Boolean dialogFreeT = waitForElementVisible(dialogTrialFree, 120);
			System.out.println("Có hiễn thị dialog trả phi không? " + dialogFreeT);
			if (dialogFreeT) {
				WebElement skipElement = driver.findElement(skip);
				skipElement.click();
			}
			Boolean isLikeButtonVisibility = waitForElementVisible(buttonAction, 20);
			System.out.println("LIke or not " + isLikeButtonVisibility);
			if (isLikeButtonVisibility) {
				int likeYoutbe = Utils.randomNumber(0, 1);
				System.out.println("LIke or not " + likeYoutbe);
				if (likeYoutbe == 1) {
					WebElement btnat = driver.findElement(buttonAction);
					smoothScrollToElement(btnat, 200, 20);
					sleepSecond(1000);
					waitForClickability(btnLikeVideo);
					sleepSecond(2000);
					smoothScrollToTop(200, 50);
				} else {

				}
			}
			sleepSecond(60000);
		}

	}
}

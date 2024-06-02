package com.vdqgpm.tests;

import org.openqa.selenium.WebDriver;

import com.vdqgpm.pages.Gmail;

public class SendGmailTest {

	private WebDriver driver;
	private Gmail gmail;

	public SendGmailTest() {
		System.setProperty("webdriver.chrome.driver", "src/resources/chromedriver.exe");

	}
}

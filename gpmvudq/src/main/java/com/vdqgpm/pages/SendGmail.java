package com.vdqgpm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vdqgpm.utilities.WaitUtils;

public class SendGmail {
	private WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private WaitUtils waitUtils;
	private By gmail = By.xpath("//a[contains(@href,'https://mail.google.com/mail/') and @class='gb_I']");
	private By dialogWriteGmail = By
			.xpath("//td[@class='Hm']//img[@class='Hq aUG' and contains(@src,'images/cleardot.gif')]");
	private By writeMail = By.xpath("//div[@class='T-I T-I-KE L3']");
	private By receiver = By.xpath("//div[@class='afx' and @aria-label]//input[@aria-label and @type='text']");
	private By title = By.xpath("//input[@name='subjectbox']");
	private By content = By.xpath("//div[@role='textbox']");
	private By loginButton = By.xpath("//div[@class='T-I J-J5-Ji aoO v7 T-I-atl L3']");

	// Constructor
	public SendGmail(WebDriver driver, long timeoutInSeconds) {
		this.driver = driver;
		this.waitUtils = new WaitUtils(driver, timeoutInSeconds);
		this.actions = new Actions(driver);
		PageFactory.initElements(driver, this);
	}

	public void moveToGmail() {
		System.out.println("SendGmail.moveToGmail()");
		Boolean isEmailVisible = waitUtils.isElementVisible(gmail);
		if (isEmailVisible) {
			driver.findElement(gmail).click();
		} else {
			System.out.println("Không tim thấy button Gmail -> Truy cập thẳng vào Gmail");
			driver.get("https://mail.google.com/mail/?tab=rm&ogbl");
		}

	}

	public void writeMail() {
		System.out.println("writeMail");
		Boolean isWriteMailDialogVisibility = waitUtils.waitForElementVisible(writeMail, 10);
		if (isWriteMailDialogVisibility) {
			WebElement element = driver.findElement(writeMail);
			element.click();
		} else {
			System.out.println("Nút Soạn Email không hiễn thị sau 10 giây");
		}
	}

	// Phương thức để nhập username
	public void enterReceiver(String email) {
		waitUtils.sleepSecond(2000);
		Boolean isReceiverVisible = waitUtils.isElementVisible(receiver);
		if (isReceiverVisible) {
			WebElement receiverElement = driver.findElement(receiver);
			System.out.println("Nhập Email");
			receiverElement.click();
			waitUtils.sendKeysSlowly(receiverElement, email, 200);
			waitUtils.sleepSecond(5000);
		} else {
			System.out.println("Không nhập được Email người nhận");
		}

	}

	// Phương thức để nhập username
	public void enterSubject(String subject) {
		WebElement subjectElement = driver.findElement(title);
		System.out.println("Nhập Tiêu đề");
		waitUtils.sendKeysSlowly(subjectElement, subject, 100);
		waitUtils.sleepSecond(5000);
	}

	// Phương thức để nhập password
	public void enterBody(String body) {
		WebElement bodyElement = driver.findElement(content);
		bodyElement.click();
		waitUtils.sendKeysSlowly(bodyElement, body, 100);
		waitUtils.sleepSecond(5000);
	}

	// Phương thức để nhấn vào nút đăng nhập
	public void clickSendEmail() {
		driver.findElement(loginButton).click();
	}
}

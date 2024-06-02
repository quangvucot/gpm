package com.vdqgpm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Gmail extends BasePage {

	private By gmail = By.xpath("//a[contains(@href,'https://mail.google.com/mail/') and @class='gb_I']");
	private By dialogWriteGmail = By
			.xpath("//td[@class='Hm']//img[@class='Hq aUG' and contains(@src,'images/cleardot.gif')]");
	private By writeMail = By.xpath("//div[@class='T-I T-I-KE L3']");
	private By receiver = By.xpath("//div[@class='afx' and @aria-label]//input[@aria-label and @type='text']");
	private By title = By.xpath("//input[@name='subjectbox']");
	private By content = By.xpath("//div[@class='Am aiL Al editable LW-avf tS-tW' and @role='textbox']");
	private By loginButton = By.xpath("//div[@class='T-I J-J5-Ji aoO v7 T-I-atl L3']");

	public Gmail(WebDriver driver, long timeoutInSeconds) {
		super(driver, timeoutInSeconds);
	}

	public void moveToGmail() {
		System.out.println("SendGmail.moveToGmail()");
		Boolean isEmailVisible = isElementVisible(gmail);
		if (isEmailVisible) {
			driver.findElement(gmail).click();
		} else {
			System.out.println("Không tim thấy button Gmail -> Truy cập thẳng vào Gmail");
			driver.get("https://mail.google.com/mail/?tab=rm&ogbl");
		}

	}

	public void writeMail() {
		System.out.println("writeMail");
		Boolean isWriteMailDialogVisibility = waitForElementVisible(writeMail, 10);
		if (isWriteMailDialogVisibility) {
			WebElement element = driver.findElement(writeMail);
			element.click();

		} else {
			System.out.println("Nút Soạn Email không hiễn thị sau 10 giây");
		}
	}

	// Phương thức để nhập username
	public void enterReceiver(String email) {
		sleepSecond(3000);
		Boolean isWriteMailDialogVisibility = waitForElementVisible(dialogWriteGmail, 30);
		if (isWriteMailDialogVisibility) {
			sleepSecond(1000);
			Boolean isReceiverVisible = isElementVisible(receiver);
			if (isReceiverVisible) {
				WebElement receiverElement = driver.findElement(receiver);
				System.out.println("Nhập Email");
				sendKeysSlowly(receiverElement, email, 200);
				sleepSecond(5000);
			} else {
				System.out.println("Không nhập được Email người nhận");
			}
		}

	}

	// Phương thức để nhập username
	public void enterSubject(String subject) {
		WebElement subjectElement = driver.findElement(title);
		System.out.println("Nhập Tiêu đề");
		sendKeysSlowly(subjectElement, subject, 100);
		sleepSecond(5000);
	}

	// Phương thức để nhập password
	public void enterBody(String body) {
		WebElement bodyElement = driver.findElement(content);
		sendKeysSlowly(bodyElement, body, 100);
		sleepSecond(5000);
	}

	// Phương thức để nhấn vào nút đăng nhập
	public void clickSendEmail() {
		driver.findElement(loginButton).click();
		sleepSecond(5000);
	}
}

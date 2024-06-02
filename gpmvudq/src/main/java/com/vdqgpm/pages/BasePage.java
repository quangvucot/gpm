package com.vdqgpm.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.vdqgpm.webdriver.WebDriverFactory;

public class BasePage {
	protected WebDriverWait wait;
	protected WebDriver driver;
	protected Actions actions;
	protected JavascriptExecutor jsExecutor;

	public BasePage(WebDriver driver, long timeoutInSeconds) {
		this.driver = WebDriverFactory.getDriver();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
		this.actions = new Actions(driver);
		PageFactory.initElements(driver, this);
		this.jsExecutor = (JavascriptExecutor) driver;
	}

	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public WebElement waitForClickability(By locator) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForUrlContains(String fraction) {
		wait.until(ExpectedConditions.urlContains(fraction));
	}

	public boolean isElementVisible(By locator) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean waitForElementVisible(By locator, long timeoutInSeconds) {
		try {
			WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
			localWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void sleepSecond(long miliSecond) {
		try {
			Thread.sleep(miliSecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Phương thức tiện ích để gửi ký tự chậm hơn
	public void sendKeysSlowly(WebElement element, String text, long delayInMillis) {
		for (char ch : text.toCharArray()) {
			element.sendKeys(String.valueOf(ch));
			try {
				Thread.sleep(delayInMillis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void openNewTab(String url) {
		// Execute JavaScript to open a new tab
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", url);
		// Switch to the new tab
		for (String tab : driver.getWindowHandles()) {
			driver.switchTo().window(tab);
		}
	}

	public void switchToTab(int index) {
		String[] tabs = driver.getWindowHandles().toArray(new String[0]);
		if (index < tabs.length) {
			driver.switchTo().window(tabs[index]);
		} else {
			throw new IllegalArgumentException("Invalid tab index");
		}
	}

	public void moveToElement(By locator) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		actions.moveToElement(element).perform();
	}

	public void clickElement(By locator) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
	}

	// Phương thức để cuộn tới vị trí cụ thể
	public void scrollTo(int x, int y) {
		jsExecutor.executeScript("window.scrollTo(arguments[0], arguments[1]);", x, y);
	}

	// Phương thức để cuộn tới cuối trang
	public void scrollToBottom() {
		jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	// Phương thức để cuộn tới một phần tử cụ thể
	public void scrollToElement(WebElement element) {
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	// Phương thức để cuộn xuống một số pixel nhất định
	public void scrollBy(int x, int y) {
		jsExecutor.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
	}

	// Phương thức lấy vị trí Y hiện tại của trang
	public int getCurrentScrollPosition() {
		return ((Long) jsExecutor.executeScript("return window.pageYOffset;")).intValue();
	}

	// Phương thức lấy chiều cao của cửa sổ (viewport)
	public int getWindowHeight() {
		return ((Long) jsExecutor.executeScript("return window.innerHeight;")).intValue();
	}

//	// Phương thức cuộn mượt mà đến một vị trí cụ thể với thời gian dừng
//	public void smoothScrollTo(int targetY, int steps, int delay) {
//		int currentY = getCurrentScrollPosition();
//		int distance = targetY - currentY;
//		int stepSize = distance / steps; // Kích thước mỗi bước cuộn
//		int pauseInterval = steps / 5; // Số bước trước khi dừng lại, điều chỉnh sao cho hợp lý
//		int pauseDuration = 1500; // Thời gian dừng lại giữa các đoạn cuộn (ms), 1 giây
//
//		for (int i = 0; i < steps; i++) {
//			currentY += stepSize;
//			jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", currentY);
//			try {
//				Thread.sleep(delay); // Độ trễ giữa các bước cuộn
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			// Dừng lại giữa các đoạn cuộn
//			if ((i + 1) % pauseInterval == 0) {
//				try {
//					Thread.sleep(pauseDuration); // Thời gian dừng lại giữa các đoạn cuộn
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
////		if (Math.abs(targetY - currentY) < Math.abs(stepSize)) {
////			currentY = targetY;
////			jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", currentY);
////			break;
////		}
//	}

	public void smoothScrollTo(int targetY, int steps, int delay) {
		int currentY = getCurrentScrollPosition();
		int distance = targetY - currentY;
		double stepSize = (double) distance / steps; // Kích thước mỗi bước cuộn

		for (int i = 0; i < steps; i++) {
			currentY += stepSize;

			// Nếu cuộn quá gần đến vị trí mục tiêu, điều chỉnh bước cuộn để không bị giật
			if (Math.abs(targetY - currentY) < Math.abs(stepSize)) {
				currentY = targetY;
			}

			jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", currentY);
			try {
				Thread.sleep(delay); // Độ trễ giữa các bước cuộn
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Đảm bảo cuộn đến đúng vị trí mục tiêu mà không bị giật
		jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", targetY);
	}
	// Phương thức lấy vị trí cuộn hiện tại

	// Phương thức cuộn mượt mà đến một phần tử cụ thể với thời gian dừng
	public void smoothScrollToElement(WebElement element, int steps, int delay) {
		int elementY = element.getLocation().getY();
		int windowHeight = getWindowHeight();
		int targetY = elementY - (windowHeight / 2) + (element.getSize().getHeight() / 2);
		smoothScrollTo(targetY, steps, delay);
	}

	// Phương thức cuộn mượt mà lên đầu trang với thời gian dừng
	public void smoothScrollToTop(int steps, int delay) {
		smoothScrollTo(0, steps, delay);
	}

	// Phương thức chờ phần tử có thể nhấp được và nhấp vào phần tử đó
	public void clickElement(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	// Phương thức cuộn đến phần tử và nhấp vào phần tử đó bằng JavaScript
	public void scrollToElementAndClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	public void jsClick(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void removeBlockingElementAndClick(WebElement element) {
		WebElement blockingElement = getBlockingElement(element);
		if (blockingElement != null) {
			System.out.println("Blocking element found: " + blockingElement.getTagName() + " "
					+ blockingElement.getAttribute("id"));
			((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none'", blockingElement);
		}

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	public void retryClick(WebElement element, int attempts) {
		wait.until(ExpectedConditions.elementToBeClickable(element));

		boolean clicked = false;
		int tryCount = 0;

		while (tryCount < attempts) {
			try {
				element.click();
				clicked = true;
				break;
			} catch (ElementClickInterceptedException e) {
				System.out.println("Attempt " + (tryCount + 1) + " failed. Retrying...");
				try {
					Thread.sleep(1000); // Chờ 1 giây trước khi thử lại
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
				tryCount++;
			}
		}

		if (!clicked) {
			throw new RuntimeException("Failed to click the element after " + attempts + " attempts.");
		}
	}

	public WebElement getBlockingElement(WebElement targetElement) {
		String script = "var elem = arguments[0];" + "var rect = elem.getBoundingClientRect();"
				+ "var x = rect.left + window.scrollX + rect.width / 2;"
				+ "var y = rect.top + window.scrollY + rect.height / 2;"
				+ "var topElem = document.elementFromPoint(x, y);" + "if (topElem !== elem) { return topElem; }"
				+ "else { return null; }";
		return (WebElement) ((JavascriptExecutor) driver).executeScript(script, targetElement);
	}

	// Phương thức để chờ và đóng thông báo
	public void closePopup(By locator) {
		boolean isClosed = false;
		while (!isClosed) {
			try {
				WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				if (popup.isDisplayed()) {
					popup.click();
					isClosed = true;
				}
			} catch (TimeoutException e) {
				System.out.println("Popup không xuất hiện, thử lại...");
			} catch (NoSuchElementException e) {
				System.out.println("Không tìm thấy phần tử, thử lại...");
			} catch (ElementClickInterceptedException e) {
				System.out.println("Phần tử bị chặn, thử lại...");
			}
			try {
				Thread.sleep(1000); // Chờ 1 giây trước khi thử lại
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}

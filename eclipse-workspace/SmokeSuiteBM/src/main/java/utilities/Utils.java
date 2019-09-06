package utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
public class Utils {

	public static WebDriver driver;
	public static ExtentTest logger;

	/**
	 * Method to invoke browser and open application url, returns the current
	 * driver instance
 	 * @return driver
	 */
	public static CommonFunctions commFunc;
	public static String baseUrl = null;
	public static Properties prop = null;
	public static String env = null;
	public static String brand = null;

	
	public static class CommonFunctions {
		static WebDriver driver;
		static FluentWait<WebDriver> fluentWait = null;
		WebDriverWait wait = null;
		WebElement element;

		public CommonFunctions(WebDriver driver) {
			this.driver = driver;
		}

		/**
		 * Common method for fluent wait
		 * 
		 * @param timeOutInSeconds
		 * @param pollingEveryInMiliSec
		 * @return fluentWait
		 */
		public static FluentWait<WebDriver> getFluentWait() {
			fluentWait = new FluentWait<WebDriver>(driver).withTimeout(Constants.SECONDS)
					.pollingEvery(Constants.MILISECONDS).ignoring(NoSuchElementException.class)
					.ignoring(NoSuchElementException.class).ignoring(ElementNotVisibleException.class)
					.ignoring(StaleElementReferenceException.class).ignoring(NoSuchFrameException.class);
			return fluentWait;
		}

		public synchronized void hoverAndSelect(WebElement hoverTo, WebElement selectTo) {
			try {
				Actions action = new Actions(driver);
				action.moveToElement(hoverTo).build().perform();
				selectTo.click();
			} catch (Exception e) {
				System.out.println("Exception is : " + e);
				throw e;
			}
		}


		public synchronized String getTextWhenReady(WebElement element, int timeout) {
			try {
				wait = new WebDriverWait(driver, timeout);
				wait.until(ExpectedConditions.visibilityOf(element));
				return element.getText().trim();
			} catch (Exception e) {
				System.out.println("Exception is : " + e);
				throw e;
			}
		}

		public synchronized void clickWhenReady(WebElement element, int timeout) {
			try {
				wait = new WebDriverWait(driver, timeout);
				wait.until(ExpectedConditions.visibilityOf(element));
				wait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
			} catch (Exception e) {
				System.out.println("Exception is : " + e);
				throw e;
			}
		}

		public synchronized String getElementAttributeValue(WebElement element, String attribute) {
			return element.getAttribute(attribute);
		}

		public synchronized int getRandomInteger(int limit) {
			Random randomGenerator = new Random();
			return randomGenerator.nextInt(limit);
		}

		public synchronized boolean isDisplayed(WebElement element, int timeout) {
			try {
				wait = new WebDriverWait(driver, timeout);
				wait.until(ExpectedConditions.visibilityOf(element));
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		public synchronized boolean verifyElementDisplayedWithinSeconds(WebElement webElement, int waitTimeOut) {
			try {
				setImplicitWait(driver, waitTimeOut);
				new WebDriverWait(driver, waitTimeOut).until(ExpectedConditions.visibilityOf(webElement));
				resetImplicitWait(driver);
				return true;
			} catch (Exception ex) {
				resetImplicitWait(driver);
				return false;
			}
		}

		public synchronized boolean verifyElementPresent(By byLocator) {
			try {
				fluentWait = getFluentWait();
				element = fluentWait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
				return true;
			} catch (Exception ex) {
				System.out.println("Element not found " + ex);
				return false;
			}
		}

		public void safeClick(WebElement element) {
			if (isElementPresent(element)) {
				element.click();
			} else {
				logger.log(Status.FAIL,
						"Element: " + element.getText() + ", is not available on a page - " + element.getLocation());
			}
		}

		public boolean isElementPresent(WebElement element) {
			if (element == null)
				throw new IllegalArgumentException("Locator cannot be Null");
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.visibilityOf(element));
				return (element.isDisplayed()) ? true : false;
			} catch (Exception e) {
				return false;
			}
		}

		public synchronized boolean verifyElementDisplayed(WebElement webElement) {
			try {
				fluentWait = getFluentWait();
				element = fluentWait.until(ExpectedConditions.visibilityOf(webElement));
				return true;
			} catch (Exception ex) {
				System.out.println("Element not found " + ex);
				return false;
			}
		}

		public synchronized boolean isElementsPresent(WebDriver driver, List<WebElement> elList) {
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy((By) elList));
				return true;
			} catch (NoSuchElementException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}

		public synchronized boolean verifyElementVisible(WebElement element) {
			try {
				fluentWait = getFluentWait();
				element = fluentWait.until(ExpectedConditions.visibilityOf(element));
				return element.isDisplayed();
			} catch (Exception ex) {
				return false;
			}
		}

//		public synchronized boolean verifyTextEquals(WebElement element, String expectedText) {
//			try {
//				String actualText = element.getText().trim();
//				if (actualText.equalsIgnoreCase(expectedText.trim())) {
//					logger.log(Status.PASS,
//							"actualText is :" + actualText + " expected text is: " + expectedText);
//					return true;
//				} else {
//					logger.log(Status.FAIL,
//							"actualText is :" + actualText + " expected text is: " + expectedText);
//					return false;
//				}
//			} catch (Exception ex) {
//				logger.log(Status.FAIL,
//						"actualText is :" + element.getText() + " expected text is: " + expectedText);
//				return false;
//			}
//		}

//		public synchronized void verifyTextEqualsString(String actualText, String expectedText) {
//			try {
//				if (actualText.equalsIgnoreCase(expectedText)) {
//					logger.log(Status.PASS,
//							"actualText is :" + actualText + " expected text is: " + expectedText);
//
//				} else {
//					logger.log(Status.FAIL,
//							"actualText is :" + actualText + " expected text is: " + expectedText);
//				}
//			} catch (Exception ex) {
//				logger.log(Status.FAIL,
//						"actualText is :" + actualText + " expected text is: " + expectedText);
//			}
//
//		}

		public synchronized void verifyTextContainsString(String actualText, String subtext) {
			try {
				if (actualText.contains(subtext.trim())) {
					logger.log(Status.PASS, "actualText :" + actualText + " verified");

				} else {
					logger.log(Status.FAIL, "actualText :" + actualText + " not verified");
				}
			} catch (Exception ex) {
				logger.log(Status.FAIL,
						"actualText is :" + actualText + " does not contain: " + subtext);
			}

		}

		public synchronized static Alert getAlert(WebDriver driver) {
			return driver.switchTo().alert();
		}

		public synchronized String getAlertText(WebDriver driver) {
			String text = getAlert(driver).getText();
			return text;
		}

		public synchronized boolean isAlertPresent(WebDriver driver) {
			try {
				driver.switchTo().alert();
				return true;
			} catch (NoAlertPresentException e) {
				return false;
			}
		}

		public synchronized void switchToWindow(String windowHandle) {

			driver.switchTo().window(windowHandle);
			waitForSeconds(2);
		}

		public synchronized Set<String> getAllOpenedWindows() {

			return driver.getWindowHandles();
		}

		public synchronized String getWindowId() {

			return driver.getWindowHandle();
		}

		public synchronized void acceptPrompt(String text, WebDriver driver) {
			try {
				if (!isAlertPresent(driver))
					return;

				Alert alert = getAlert(driver);
				alert.sendKeys(text);
				alert.accept();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToFrame(WebElement element, WebDriver driver) {
			try {
				driver.switchTo().frame(element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToFrame(WebElement element) {
			try {
				driver.switchTo().frame(element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void enterFullScreen() {
			driver.manage().window().fullscreen();
			waitForSeconds(1);
		}

		public synchronized void exitFullScreen() {
			driver.manage().window().maximize();
			waitForSeconds(1);
		}

		public synchronized void switchToDefaultContent(WebDriver driver) {
			try {
				driver.switchTo().defaultContent();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToDefaultContent() {
			try {
				driver.switchTo().defaultContent();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void sendEnterKey() {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ENTER);
		}

		public synchronized void goBack() {
			try {
				driver.navigate().back();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void goForward() {
			try {
				driver.navigate().forward();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void refresh() {
			try {
				driver.navigate().refresh();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized Set<String> getWindowHandlens() {
			try {
				return driver.getWindowHandles();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToWindow(int index) {
			try {
				LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());

				if (index < 0 || index > windowsId.size()) {
					throw new IllegalArgumentException("Invalid Index : " + index);
				}
				driver.switchTo().window(windowsId.get(index));
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToParentWindow() {
			try {
				LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());
				driver.switchTo().window(windowsId.get(0));
				System.out.println("The user is successfully switched to parent window");
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToParentWithChildClose() {
			try {
				LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());

				for (int i = 1; i < windowsId.size(); i++) {
					System.out.println(windowsId.get(i));
					driver.switchTo().window(windowsId.get(i));
					driver.close();
				}

				switchToParentWindow();

			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void switchToFrame(String nameOrId) {
			try {
				driver.switchTo().frame(nameOrId);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void selectUsingVisibleValue(WebElement element, String visibleValue) {
			try {
				Select select = new Select(element);
				select.selectByVisibleText(visibleValue);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized String getSelectedValue(WebElement element) {
			String value = null;
			try {
				value = new Select(element).getFirstSelectedOption().getText();
			} catch (Exception e) {
				throw (e);
			}
			return value;
		}

		public synchronized void selectUsingIndex(WebElement element, int index) {
			try {
				Select select = new Select(element);
				select.selectByIndex(index);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void selectUsingVisibleText(WebElement element, String text) {
			try {
				Select select = new Select(element);
				select.selectByVisibleText(text);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized List<String> getAllDropDownValues(WebElement element) {
			List<String> valueList = null;
			try {
				Select select = new Select(element);
				List<WebElement> elementList = select.getOptions();
				valueList = new LinkedList<String>();

				for (WebElement ele : elementList) {
					// System.out.println(ele.getText());
					valueList.add(ele.getText());
				}
			} catch (Exception e) {
				throw (e);
			}
			return valueList;
		}

		public synchronized String readValueFromElement(WebElement element) {
			try {
				String text = element.getText();
				return text;
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized String readAttributeValue(WebElement element) {
			String value = null;
			try {
				if (null == element) {
					return null;
				}
				if (!isDisplayed(element)) {
					return null;
				}
				value = element.getAttribute("value");
			} catch (Exception e) {
				throw (e);
			}
			return value;
		}

		public synchronized boolean isDisplayed(WebElement element) {
			try {
				element.isDisplayed();
				return true;
			} catch (Exception e) {
				Reporter.log(e.fillInStackTrace().toString());
				return false;
			}
		}

//		public synchronized boolean isDisplayedwithString(WebElement element, String string) {
//			try {
//				if (element.isDisplayed()) {
//					logger.log(Status.PASS, string);
//					return true;
//				} else {
//					logger.log(Status.FAIL, string);
//				}
//			} catch (Exception ex) {
//				logger.log(Status.FAIL, string);
//			}
//			return false;
//		}

		protected synchronized boolean isNotDisplayed(WebElement element) {
			try {
				element.isDisplayed();
				return false;
			} catch (Exception e) {
				Reporter.log(e.fillInStackTrace().toString());
				return true;
			}
		}

		public synchronized void clickUsingJS(WebElement element) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized Object executeScript(String script) {
			try {
				JavascriptExecutor exe = (JavascriptExecutor) driver;
				return exe.executeScript(script);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized Object executeScript(String script, Object... args) {
			try {
				JavascriptExecutor exe = (JavascriptExecutor) driver;
				return exe.executeScript(script, args);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollToElement(WebElement element) {
			try {
				executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x,
						element.getLocation().y);
				System.out.println(element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollToElemetAndClick(WebElement element) {
			try {
				scrollToElement(element);
				element.click();
				System.out.println(element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void waitForSeconds(int seconds) {
			try {
				Thread.sleep(seconds * 100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public synchronized void scrollIntoView(WebElement element) {
			try {
				executeScript("arguments[0].scrollIntoView()", element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollIntoViewAndClick(WebElement element) {
			try {
				scrollIntoView(element);
				element.click();
				System.out.println(element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollDownVertically() {
			executeScript("window.scrollTo(0, document.body.scrollHeight)");

		}

		public synchronized void scrollUpVertically() {
			executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		}

		/**
		 * Method to scroll down to x position
		 * 
		 * @param driver
		 * @throws Exception
		 */
		public synchronized void scrollDownByxPostion(WebDriver driver, int xPos) throws Exception {
			try {
				executeScript("scroll(0, " + xPos + ")");
				Thread.sleep(500);
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Method to scroll Up to x position
		 * 
		 * @param driver
		 * @throws Exception
		 */
		public synchronized void scrollUpByxPostion(WebDriver driver, int xPos) throws Exception {
			try {
				executeScript("scroll(0, - " + xPos + ")");
				Thread.sleep(500);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void zoomInByPercentage() {
			executeScript("document.body.style.zoom='40%'");
		}

		public synchronized void zoomBy100Percentage() {
			executeScript("document.body.style.zoom='100%'");
		}

		public synchronized void setImplicitWait(long timeout, TimeUnit unit) {
			try {
				driver.manage().timeouts().implicitlyWait(timeout, unit == null ? TimeUnit.SECONDS : unit);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void setPageLoadTimeout(long timeout, TimeUnit unit) {
			try {
				driver.manage().timeouts().pageLoadTimeout(timeout, unit == null ? TimeUnit.SECONDS : unit);
			} catch (Exception e) {
				throw (e);
			}
		}

		private synchronized WebDriverWait getWait() {
			try {
				wait = new WebDriverWait(driver, 30);
				wait.pollingEvery(1, TimeUnit.MILLISECONDS);
				wait.ignoring(NoSuchElementException.class);
				wait.ignoring(ElementNotVisibleException.class);
				wait.ignoring(StaleElementReferenceException.class);
				wait.ignoring(NoSuchFrameException.class);
			} catch (Exception e) {
				throw (e);
			}
			return wait;
		}

		public synchronized WebElement waitForElementToBeClickable(WebElement element) {
			try {
				fluentWait = getFluentWait();
				getWaitForSeconds(2);
				return wait.until(ExpectedConditions.elementToBeClickable(element));
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized WebDriverWait getWaitForSeconds(long waitTimeOutInSeconds) {
			try {
				new WebDriverWait(driver, waitTimeOutInSeconds);
			} catch (Exception e) {
				throw (e);
			}
			return wait;
		}

		public synchronized WebElement waitUntilClickable(WebDriver driver, By by) {
			WebElement element = null;
			try {
				fluentWait = getFluentWait();
				element = wait.until(ExpectedConditions.elementToBeClickable(by));
			} catch (Exception e) {
				throw (e);
			}
			return element;
		}

		public synchronized WebElement waitForVisible(WebDriver driver, By by) {
			WebElement element = null;
			try {
				WebDriverWait wait = new WebDriverWait(driver, 10);
				element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			} catch (Exception e) {
				throw (e);
			}
			return element;
		}

		public synchronized void click(WebDriver driver, By by) {
			try {
				waitUntilClickable(driver, by);
				driver.findElement(by).click();
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void enterText(WebDriver driver, By by, String text) {
			try {
				waitForVisible(driver, by);
				driver.findElement(by).sendKeys(text);
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Enters Text inside the Feilds.
		 * 
		 * @param element
		 */
		public void enterFieldText(WebElement element, String expectedText) {
			if (isElementPresent(element)) {
				clickUsingJS(getFluentWait().until(ExpectedConditions.elementToBeClickable(element)));
				element.clear();
				element.sendKeys(expectedText);
			} else {
				logger.log(Status.FAIL,
						"Element: " + element.getText() + ", is not available on a page - " + element.getLocation());
			}
		}

		public synchronized void enterText(WebDriver driver, WebElement by, String text) {
			try {
				by.sendKeys(text);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollTo(WebDriver driver, By by) {
			try {
				WebElement element = driver.findElement(by);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView", element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void acceptAlert(WebDriver driver) {
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public synchronized void dismissAlert(WebDriver driver) {
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public synchronized boolean isAlertPresent(WebDriver driver, By by) {
			boolean present = false;
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.alertIsPresent());
				present = true;
			} catch (Exception e) {
				e.printStackTrace();
				present = false;
			}
			return present;
		}

		public synchronized String getSaltString() {
			String saltStr = null;
			try {
				String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
				StringBuilder salt = new StringBuilder();
				Random rnd = new Random();
				while (salt.length() < 18) { // length of the random string.
					int index = (int) (rnd.nextFloat() * SALTCHARS.length());
					salt.append(SALTCHARS.charAt(index));
				}
				saltStr = salt.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return saltStr;

		}

		/*
		 * public static WebElement waitFluentlyForElement(WebDriver driver,
		 * WebElement element, int timeOutInSeconds) { try{ //To use
		 * WebDriverWait(), we would have to nullify implicitlyWait(). //Because
		 * implicitlyWait time also set "driver.findElement()" wait time. //info
		 * from:
		 * https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/
		 * 6VO_7IXylgY driver.manage().timeouts().implicitlyWait(0,
		 * TimeUnit.SECONDS); //nullify implicitlyWait() Wait wait = new
		 * FluentWait(driver).withTimeout(timeOutInSeconds,
		 * TimeUnit.SECONDS).pollingEvery(5,
		 * TimeUnit.SECONDS).ignoring(NoSuchElementException.class); element =
		 * (WebElement) wait.until(ExpectedConditions.visibilityOf(element));
		 * driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE,
		 * TimeUnit.SECONDS); //reset implicitlyWait return element; //return
		 * the element } catch (Exception e) { e.printStackTrace(); } return
		 * null; }
		 */

		// This method waits for a particular element to disappear
		public synchronized void waitForElementToDisappear(By by) {
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.invisibilityOfElementLocated(by));
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Waits for the Condition of JavaScript.
		 *
		 *
		 * @param WebDriver
		 *            The driver object to be used to wait and find the element
		 * @param String
		 *            The javaScript condition we are waiting. e.g. "return
		 *            (xmlhttp.readyState >= 2 && xmlhttp.status == 200)"
		 * @param int
		 *            The time in seconds to wait until returning a failure
		 * 
		 * @return boolean true or false(condition fail, or if the timeout is
		 *         reached)
		 **/
		public synchronized boolean waitForJavaScriptCondition(WebDriver driver, final String javaScript,
				int timeOutInSeconds) {
			boolean jscondition = false;
			try {
				new WebDriverWait(driver, timeOutInSeconds) {
				}.until(new ExpectedCondition<Boolean>() {

					@Override
					public Boolean apply(WebDriver driverObject) {
						return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
					}
				});
				jscondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);
				driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset

			} catch (Exception e) {
				throw (e);
			}
			return jscondition;
		}

		/**
		 * Waits for the completion of Ajax jQuery processing by checking
		 * "return jQuery.active == 0" condition.
		 *
		 * @param WebDriver
		 *            - The driver object to be used to wait and find the
		 *            element
		 * @param int
		 *            - The time in seconds to wait until returning a failure
		 * 
		 * @return boolean true or false(condition fail, or if the timeout is
		 *         reached)
		 */
		public synchronized boolean waitForJQueryProcessing(WebDriver driver, int timeOutInSeconds) {
			boolean jQcondition = false;
			try {
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
																				// implicitlyWait()
				new WebDriverWait(driver, timeOutInSeconds) {
				}.until(new ExpectedCondition<Boolean>() {

					@Override
					public Boolean apply(WebDriver driverObject) {
						return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
					}
				});
				jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
				driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); // reset
				// implicitlyWait
				return jQcondition;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jQcondition;
		}

		/**
		 * Coming to implicit wait, If you have set it once then you would have
		 * to explicitly set it to zero to nullify it -
		 */
		public synchronized void nullifyImplicitWait(WebDriver driver) {
			try {
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Set driver implicitlyWait() time.
		 */
		public synchronized void setImplicitWait(WebDriver driver, int waitTime_InSeconds) {
			try {
				driver.manage().timeouts().implicitlyWait(waitTime_InSeconds, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Reset ImplicitWait. To reset ImplicitWait time you would have to
		 * explicitly set it to zero to nullify it before setting it with a new
		 * time value.
		 */
		public synchronized void resetImplicitWait(WebDriver driver) {
			try {
				driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				driver.manage().timeouts().implicitlyWait(Constants.DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS);
			} catch (Exception e) {
				throw (e);
			}
		}

		public boolean isElementClickable(WebDriver driver, WebElement locator) {
			return (locator.isDisplayed()) ? true : false;
		}

		public boolean isElementPresent(WebDriver driver, WebElement element) {
			if (element == null)
				throw new IllegalArgumentException("Locator cannot be Null");
			try {
				WebDriverWait wait = new WebDriverWait(driver, 5);
				wait.until(ExpectedConditions.visibilityOf(element));
				return (element.isDisplayed()) ? true : false;
			} catch (Exception e) {
				return false;
			}
		}

		/**************** Methods *************/

		/**
		 * Check hover message text
		 * 
		 * @param driver
		 * @param element
		 * @return string
		 */
		public synchronized String checkHoverMessage(WebElement element) {
			try {
				String tooltip = element.getAttribute("title");
				return tooltip;
			} catch (Exception e) {
				throw (e);
			}
		}

		/**************** Methods *************/

		/**
		 * Check hover
		 * 
		 * @param driver
		 * @param element
		 * @return string
		 */

		public synchronized void mousehover(WebElement element) {
			try {
				Actions action = new Actions(driver);
				action.moveToElement(element).build().perform();
			} catch (Exception e) {
				throw (e);
			}
		}

		public boolean breadcrumbhighlight(WebElement element) {
			try {
				Actions action = new Actions(driver);
				action.moveToElement(element).build().perform();
			} catch (Exception e) {
				throw (e);
			}
			return false;
		}

		public synchronized void mouseHover(WebElement hoverTo, int timeout) {
			try {
				wait = new WebDriverWait(driver, timeout);
				wait.until(ExpectedConditions.visibilityOf(hoverTo));
				Actions action = new Actions(driver);
				action.moveToElement(hoverTo).build().perform();
			} catch (Exception e) {
				System.out.println("Exception is : " + e);
				throw e;
			}
		}

		/**
		 * Helper method: looks through a list of WebElements, to find the first
		 * WebElement with matching xPath
		 * 
		 * @param elements
		 * @param Class
		 * @return WebElement or null
		 */
		public synchronized WebElement findElementByClassNameFromElements(List<WebElement> elements, String className) {
			WebElement result = null;
			for (WebElement element : elements) {
				if (element.getAttribute("class").contains(className)) {
					result = element;
					break;
				}
			}
			return result;
		}

		/**
		 * Helper method: looks through a list of WebElements, to find the first
		 * WebElement with matching text
		 * 
		 * @param elements
		 * @param text
		 * @return WebElement or null
		 */
		public synchronized WebElement findElementByTextFromElements(List<WebElement> elements, String text) {
			WebElement result = null;
			for (WebElement element : elements) {
				element.getText().trim();
				if (text.equalsIgnoreCase(element.getText().trim())) {
					result = element;
					break;
				}
			}
			return result;
		}

		/**
		 * MEthod to get response code of link URL. //Link URL Is valid If found
		 * response code = 200. //Link URL Is Invalid If found response code =
		 * 404 or 505.
		 * 
		 * @param chkurl
		 * @return validResponse
		 * @throws ClientProtocolException
		 * @throws IOException
		 */
		public synchronized boolean getResponseCode(String chkurl) throws ClientProtocolException, IOException {
			boolean validResponse = false;
			try {

				CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
						.build();

				HttpGet getMethod = new HttpGet(chkurl);
				HttpResponse response = httpClient.execute(getMethod);

				// Get response code of URL HttpResponse
				int resp_Code = response.getStatusLine().getStatusCode();
				System.out.println("Response Code Is : " + resp_Code);

				if ((resp_Code == 404) || (resp_Code == 505)) {
					validResponse = false;
				} else {
					validResponse = true;
				}

				/*
				 * Assert.assertEquals(response.getStatusLine().getStatusCode(),
				 * 200);
				 */
			} catch (Exception e) {
				throw (e);
			}
			return validResponse;
		}

		/**
		 * Method to highlight the found element with green dashed border
		 * 
		 * @param WebDriver,
		 *            element
		 * @return WebElement
		 * @throws Exception
		 */
		public synchronized void highlightElement(WebElement element) throws Exception {
			try {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
						"border:1px dotted springgreen !important;");
			} catch (Exception e) {
				CustomException.throwException(e, "Element not found");
				throw (e);
			}
		}

		/**
		 * Method to hover and click on element
		 * 
		 * @param hoverElement
		 * @param locatorElement
		 * @throws Exception
		 */
		public synchronized void hoverAndClickElement(WebElement hoverElement, WebElement locatorElement)
				throws Exception {
			try {
				Actions action = new Actions(driver);

				action.moveToElement(hoverElement).click().build().perform();

				// ((JavascriptExecutor)
				// driver).executeScript("arguments[0].scrollIntoView(true);",
				// element);
				Thread.sleep(500);

				locatorElement.click();

			} catch (Exception e) {
				CustomException.throwException(e, "Element not found");
				throw (e);
			}
		}

		/**
		 * Compact way to verify if an elements is on the page
		 * 
		 * @param driver
		 * @param by
		 * @return boolean value
		 */
		public synchronized boolean isElementsPresent(WebDriver driver, By by) {
			try {
				fluentWait = getFluentWait();
				fluentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
				return true;
			} catch (NoSuchElementException e) {
				return false;
			} catch (Exception e) {
				return false;
			}
		}

		/**
		 * Select radio button
		 * 
		 * @param driver
		 * @param by
		 * @param value
		 */
		public synchronized static void selectRadioButton(List<WebElement> elementList, String value) {
			try {
				for (WebElement radio : elementList) {
					if (radio.getAttribute("value").equalsIgnoreCase(value)) {
						radio.click();
					}
				}
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Method to check broken links on the page per href
		 * 
		 * @return boolean value
		 * @throws Exception
		 */
		public synchronized boolean checkBrokenLinks(WebDriver driver, List<WebElement> total_links) throws Exception {
			try {

				// for loop to open all links one by one to check response code.
				boolean isValid = false;

				for (int i = 0; i < total_links.size(); i++) {
					String url = total_links.get(i).getAttribute("href");
					String linkTxt = total_links.get(i).getText();

					if (url != null) {

						// Call getResponseCode function for each URL to check
						// response code.
						isValid = getResponseCode(url);

						// Print message based on value of isValid which Is
						// returned by getResponseCode
						// function.
						if (isValid) {
							logger.log(Status.PASS,
									"| <b>" + linkTxt + "</b> | Valid link: " + url);
						} else {
							logger.log(Status.FAIL,
									"| <b>" + linkTxt + "</b> | Valid link: " + url);
						}
					} else {
						// If <a> tag do not contain href attribute and value
						// then print this message
						logger.log(Status.INFO, "String null");
						continue;
					}
				}
				return isValid;

			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Method to click on value from list of WebElements
		 * 
		 * @param driver
		 * @param elList
		 * @param value
		 * @return
		 * @throws Exception
		 * 
		 */
		public synchronized boolean clickValueFromList(List<WebElement> elList, WebDriver driver, String value)
				throws Exception {
			boolean optionFound = false;

			try {
				Thread.sleep(500);
				for (WebElement option : elList) {
					if (value.equalsIgnoreCase(option.getText())) {
						option.click();
						logger.log(Status.INFO, "| Selected Value: " + value);
						optionFound = true;
						break;
					}
				}
			} catch (Exception e) {
				logger.log(Status.FAIL, "| Element not Found |");
				return false;
			}
			return optionFound;
		}

		/**
		 * Method to generate random Name
		 * 
		 * @param driver
		 * @return email
		 */
		public String randomNameGenerator() {
			String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuffer randStr = new StringBuffer();
			for (int i = 0; i < 6; i++) {
				Random randomGenerator = new Random();
				int number = randomGenerator.nextInt(10);
				char ch = CHAR_LIST.charAt(number);
				randStr.append(ch);
			}
			return randStr.toString();
		}

		/**
		 * Method to generate random email
		 * 
		 * @param driver
		 * @return email
		 */

		public synchronized String generateRandomEmail(WebDriver driver) {
			try {
				String email = null;
				String expfname = randomNameGenerator();
				email = expfname + Math.round(Math.random() * 1357) + "@gmail.com";
				Assert.assertFalse(email.isEmpty(), "| Random Email not generated");
				logger.log(Status.INFO, "| Random Email :" + email);
				return email;

			} catch (AssertionError e) {
				logger.log(Status.FAIL, "| Random Email not generated");
				throw (e);
			}
		}

		public synchronized String generateRandomEmail() {
			try {
				String email = null;
				String expfname = randomNameGenerator();
				email = expfname + String.valueOf(Math.round(Math.random() * 1357)) + "accept@gmail.com";
				Assert.assertFalse(email.isEmpty(), "| Random Email not generated");
				logger.log(Status.INFO, "| Random Email :" + email);
				return email;

			} catch (AssertionError e) {
				logger.log(Status.FAIL, "| Random Email not generated");
				throw (e);
			}
		}

		/**
		 * Method to get absolute xpath from element
		 * 
		 * @param element
		 * @param driver
		 * @return absolute xpath
		 */
		public synchronized String getAbsoluteXPath(WebElement element, WebDriver driver) {
			return (String) ((JavascriptExecutor) driver)
					.executeScript("function absoluteXPath(element) {" + "var comp, comps = [];" + "var parent = null;"
							+ "var xpath = '';" + "var getPos = function(element) {" + "var position = 1, curNode;"
							+ "if (element.nodeType == Node.ATTRIBUTE_NODE) {" + "return null;" + "}"
							+ "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {"
							+ "if (curNode.nodeName == element.nodeName) {" + "++position;" + "}" + "}"
							+ "return position;" + "};" +

							"if (element instanceof Document) {" + "return '/';" + "}" +

							"for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
							+ "comp = comps[comps.length] = {};" + "switch (element.nodeType) {"
							+ "case Node.TEXT_NODE:" + "comp.name = 'text()';" + "break;" + "case Node.ATTRIBUTE_NODE:"
							+ "comp.name = '@' + element.nodeName;" + "break;"
							+ "case Node.PROCESSING_INSTRUCTION_NODE:" + "comp.name = 'processing-instruction()';"
							+ "break;" + "case Node.COMMENT_NODE:" + "comp.name = 'comment()';" + "break;"
							+ "case Node.ELEMENT_NODE:" + "comp.name = element.nodeName;" + "break;" + "}"
							+ "comp.position = getPos(element);" + "}" +

							"for (var i = comps.length - 1; i >= 0; i--) {" + "comp = comps[i];"
							+ "xpath += '/' + comp.name.toLowerCase();" + "if (comp.position !== null) {"
							+ "xpath += '[' + comp.position + ']';" + "}" + "}" +

							"return xpath;" +

							"} return absoluteXPath(arguments[0]);", element);
		}

		/**
		 * Method to get the list of web elements one by one and store into
		 * array string
		 * 
		 * @param driver
		 * @param elList
		 * @return ArrayList<String>
		 */
		public synchronized ArrayList<String> getArrayListFromWebElementList(WebDriver driver,
				List<WebElement> elList) {
			try {
				ArrayList<String> storelist = new ArrayList<String>();
				int i = 0;

				// Get element one by one from list of web elements
				for (WebElement element : elList) {

					if (i <= elList.size()) {
						String xPath = getAbsoluteXPath(element, driver);
						storelist.add(xPath);
					}

					i++;
				}

				return storelist;

			} catch (Exception e) {
				logger.log(Status.FAIL, "| Element not Found in List ");
				throw (e);
			}
		}

		public synchronized void selectUsingValue(WebElement element, String value) {
			try {
				Select select = new Select(element);
				select.selectByValue(value);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void waitForElementToBeDisplayed(WebElement element, int timeout) {
			try {
				wait = new WebDriverWait(driver, timeout);
				wait.until(ExpectedConditions.visibilityOf(element));
			} catch (Exception e) {
				System.out.println("Exception is : " + e);
				throw e;
			}
		}

		/**
		 * Takes controls on new tab
		 * 
		 * @param driver
		 * @return window0
		 */
		public synchronized String handleNewTab(WebDriver driver) {
			try {
				Set<String> allWindowHandles = driver.getWindowHandles();
				String window0 = (String) allWindowHandles.toArray()[1];
				driver.switchTo().window(window0);
				return window0;
			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Method to select value from Menu
		 * 
		 * @param driver
		 * @param by,
		 *            locator
		 * @return element
		 * @throws Exception
		 */
		public synchronized WebElement hoverAndSelectValue(WebDriver driver, WebElement hoverElement,
				WebElement locatorElement) throws Exception {
			try {
				Actions actions = new Actions(driver);
				actions.moveToElement(hoverElement).build().perform();
				Thread.sleep(1000);
				actions.moveToElement(locatorElement);
				locatorElement.click();

				return locatorElement;

			} catch (Exception e) {
				logger.log(Status.FAIL, "| Element not Found |");
				throw (e);
			}
		}

		/**
		 * Method to find element by locator and check if it is invisible,
		 * return true if found else false
		 * 
		 * @param driver
		 * @param by
		 * @return boolean value
		 * @throws Exception
		 */
		public synchronized boolean isElementInvisible(WebDriver driver, By by) throws Exception {
			try {
				fluentWait = getFluentWait();
				return fluentWait.until(ExpectedConditions.invisibilityOfElementLocated(by));

			} catch (Exception e) {
				logger.log(Status.FAIL, "Element not found");
				return false;
			}
		}

		/**
		 * Takes controls on new window and check links redirecting to
		 * respective page
		 * 
		 * @param driver
		 * @return boolean
		 * @throws Exception
		 */
		public synchronized boolean verifyElementsInNewWindow(WebElement el, String url, WebDriver driver)
				throws Exception {
			try {
				Actions newTab = new Actions(driver);
				boolean setValue;

				newTab.keyDown(Keys.SHIFT).click(el).keyUp(Keys.SHIFT).build().perform();
				Thread.sleep(3000);

				// Handle windows change
				String base = driver.getWindowHandle();
				Set<String> set = driver.getWindowHandles();

				set.remove(base);
				assert set.size() == 1;
				driver.switchTo().window((String) set.toArray()[0]);

				Thread.sleep(2000);
				logger.log(Status.INFO, url + "| Redirected URL: " + driver.getCurrentUrl());

				if (driver.getCurrentUrl().contains(url)) {
					setValue = true;
				} else {
					setValue = false;
				}

				// close the window
				driver.close();
				driver.switchTo().window(base);

				// handle windows change and switch back to the main
				// window
				Thread.sleep(2500);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
				}
				Thread.sleep(500);
				return setValue;

			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * method to verify the text is not equal to another string
		 * 
		 */

		public synchronized void verifyTextNotEquals(String actualText, String expectedText) {
			try {

				if (!actualText.equalsIgnoreCase(expectedText)) {
					logger.log(Status.PASS,
							"actualText is :" + actualText + " expected text is: " + expectedText);

				} else {
					logger.log(Status.FAIL,
							"actualText is :" + actualText + " expected text is: " + expectedText);
				}
			} catch (Exception ex) {
				logger.log(Status.FAIL,
						"actualText is :" + actualText + " expected text is: " + expectedText);
			}

		}

		/**
		 * Takes controls on new tab and check links redirecting to respective
		 * page
		 * 
		 * @param driver
		 * @return boolean
		 * @throws Exception
		 */
		public synchronized boolean verifyElementsInSameWindow(WebElement el, String url, WebDriver driver,
				WebDriverWait wait) throws Exception {
			try {
				Actions newTab = new Actions(driver);
				boolean setValue;

				// highlightElement(el);

				newTab.keyDown(Keys.CONTROL).keyDown(Keys.SHIFT).click(el).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build()
						.perform();
				Thread.sleep(5000);

				// handle windows change
				String base = driver.getWindowHandle();
				Set<String> set = driver.getWindowHandles();

				set.remove(base);
				assert set.size() == 1;
				driver.switchTo().window((String) set.toArray()[0]);

				// close the window and sitch back to the base tab
				driver.close();
				driver.switchTo().window(base);

				logger.log(Status.INFO, url + "| Redirected URL: " + driver.getCurrentUrl());

				if (driver.getCurrentUrl().contains(url)) {
					setValue = true;
				} else {
					setValue = false;
				}

				return setValue;

			} catch (Exception e) {
				throw (e);
			}
		}

		/**
		 * Method to wait for page load
		 */

		public void sid1WaitForPageLoaded() {
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		}

		public void sidWaitForPageLoaded() {
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
							.equals("complete");
				}
			};
			Wait<WebDriver> wait = new WebDriverWait(driver, 30);
			try {
				wait.until(expectation);
			} catch (Throwable error) {
				Assert.assertFalse(true, "Timeout waiting for Page Load Request to complete.");
			}
		}

		public void waitForPageLoaded() {
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
							.equals("complete");
				}
			};
			try {
				WebDriverWait wait = new WebDriverWait(driver, 5);
				wait.until(expectation);
			} catch (Throwable error) {
				System.out.println("Timeout waiting for Page Load Request to complete.");
			}
			try {
				// driver.manage().timeouts().implicitlyWait(5,
				// TimeUnit.SECONDS);
				if (driver.findElement(By.xpath("//*[contains(@class,'loading')]")).isDisplayed()) {
					waitForSeconds(6);
				}
			} catch (NoSuchElementException a) {

			}
		}

		public synchronized void hiddenElementSendKeys(WebElement element, String keys) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].value='keys';", element);
			} catch (Exception e) {
				throw (e);
			}
		}

		public synchronized void scrollTillPageEnd() {
			executeScript("window.scrollTo(0, document.body.scrollHeight)");
			waitForSeconds(1);

		}

	}

	/**
	 * This class contains custom methods to throw exception, assertion error
	 * and take screenshots.
	 * 
	 * @author hmarkam
	 */
	public static class CustomException {

		/**
		 * Custom assertion method to print failure message and take screenshots
		 * with calling method name.
		 * 
		 * @param a
		 * @param errorMsg
		 * @throws AssertionError
		 */
		public synchronized static void throwAssertionError(AssertionError a, String errorMsg) throws AssertionError {
			logger.log(Status.FAIL, errorMsg);
			throw a;
		}

		/**
		 * Custom exception method to print failure message and take screenshots
		 * with calling method name.
		 * 
		 * @param e
		 * @param errorMsg
		 * @throws Exception
		 */
		public synchronized static void throwException(Exception e, String errorMsg) throws Exception {
			logger.log(Status.FAIL, errorMsg);
			throw e;
		}

	}

	/**
	 * This class contains Soft Assertion validation methods and unimplemented
	 * methods.
	 * 
	 * @author hmarkam
	 */

	public static class TestSoftAssert extends SoftAssert {

		public List<String> messages = new ArrayList<>();

		@Override
		public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
			messages.add("onAssertFailure");
		}

		@Override
		public void assertAll() {
			try {
				messages.add("assertAll");
				super.assertAll();
			} catch (AssertionError e) {
				throw (e);
			}
		}

		/**
		 * Assertion method to Soft Assert the validation and log the respective
		 * result.
		 * 
		 * @param condition,
		 *            failureMessage
		 * @throws Exception
		 */

		public synchronized void softAssertEquals(int actual, int expected, String failureMessage) {

			this.assertEquals(actual, expected, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				logger.log(Status.FAIL, failureMessage);
			} else {
				logger.log(Status.PASS, failureMessage.replace("not", ""));
			}

			messages.clear();
		}

		public synchronized void softAssertTrue(Boolean condition, String failureMessage) {

			this.assertTrue(condition, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				logger.log(Status.FAIL, failureMessage);
			} else {
				logger.log(Status.PASS, failureMessage.replace("not", ""));
			}

			messages.clear();
		}

		public synchronized void softAssertFalse(Boolean condition, String failureMessage) {

			this.assertFalse(condition, failureMessage);

			if (this.messages.contains("onAssertFailure")) {
				logger.log(Status.FAIL, failureMessage);
			} else {
				logger.log(Status.PASS, failureMessage.replace("not", ""));
			}

			messages.clear();
		}

		/**
		 * @author Shiva Function to wait for loader
		 * @throws Exception
		 */

		public void WaitForLoaderToFinish(WebDriver driver) throws Exception {

			try {
				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOf(
						driver.findElement(By.xpath("//div[@class='asc-global-indicator']/div[@class='loader']"))));
				Thread.sleep(2000);
				logger.log(Status.INFO, "Loader dissapeared.");
			} catch (Exception e) {
				logger.log(Status.INFO, "Loader not found.");
				throw new Exception(e);
			}
		}

	}


//	
//	
	public static boolean isDiplayedElement(WebElement element, int waitTimeOut) {
		try {
			element.isDisplayed();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
//	/*Use for 3 level navigation*/


	public static void hoverAndSelect3(WebElement l1Category, WebElement l2Category, WebElement l3Category) {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(l1Category).moveToElement(l2Category).build().perform();
			l3Category.click();
		} catch (Exception e) {
			System.out.println("Exception is : " + e);
			throw e;
		}
		
	}



}

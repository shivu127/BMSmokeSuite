package com.bm.qa.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.bm.qa.base.TestBase;


public class HomePage {

	WebDriver driver;
	//ExtentTest logger;

	//Page Factory
	@FindBy(xpath="/html//ul[@id='options']/li[1]/div/ul/li[@class='hidden-xs']/a[1]")
	WebElement login;
	
	@FindBy(xpath ="//ul[@id='options']/li/div/ul/li[2]/a[2]")
	WebElement RegistrationLink;
	
	@FindBy(xpath="//a[@id='shoppingCartLink']/i")
	WebElement carticon;
	
	@FindBy(xpath="/html/body/app-root//app-header//div[@class='col-lg-5 col-md-6 pull-right']/ul//a[@href='/phone-numbers-addresses']")
	WebElement callUslink;
	
	@FindBy(linkText="STORE LOCATOR")
	WebElement storeLocator;
	
	@FindBy(xpath="//div[@id='florida']//h2[@class='text-left']")
	WebElement florida;
	
	@FindBy(xpath="//button//span[@class=' temp-modal']")
	WebElement closeMemberpopUp;
	
	public HomePage(WebDriver driver) throws IOException {
		//this.logger=logger;
		this.driver=driver;
		PageFactory.initElements(driver, this);

	}
	public String validatePageTitle() {
		return driver.getTitle();
	}
	
	public HomePage verifyTheloginlink1() {
		RegistrationLink.isDisplayed();
		return null;
	}
	public MyAccountPage verifyTheloginlink() {

		RegistrationLink.click();
		return new MyAccountPage(driver);
	}
	
	public void clickOnRegisterLink() throws Exception {
		System.out.println("Hi Before clicking register link");
		RegistrationLink.click();
		//logger.log(Status.INFO, "Clicked on register");
	}
	
	public String[] clickOnFooterLinks() throws InterruptedException {
		String[] retVal ;
		List<String> lst = new ArrayList();
		int length=driver.findElements(By.xpath("//ul[@class='list-inline list-unstyled']//ul[@class='list-unstyled']//li")).size();
		for(int i=1;i<=length;i++) {
		//	String text=driver.findElement(By.xpath("(//ul[@class='list-inline list-unstyled']//ul[@class='list-unstyled']//li)["+i+"]")).getText();

			//To scroll to the footer section
			WebElement element = driver.findElement(By.xpath("//h6[text()='Our Company']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(1000); 
			//To get the current window handle 
			String getWindowHandle = driver.getWindowHandle();
			
			driver.findElement(By.xpath("(//ul[@class='list-inline list-unstyled']//ul[@class='list-unstyled']//li//a)["+i+"]")).click();
			
			Set<String> getAllwindowHandles = driver.getWindowHandles();
			
			if(getAllwindowHandles.size()>1) {
				for(String handle: getAllwindowHandles){
		            if(!handle.equals(getWindowHandle)){
		            driver.switchTo().window(handle);
		            Thread.sleep(1000);
		            String url=driver.getCurrentUrl();
					lst.add(url);
		            driver.close();
		            }
		            driver.switchTo().window(getWindowHandle);
		        }
				
			}else {
				Thread.sleep(1000);
				String txt=driver.getCurrentUrl();
				lst.add(txt.replace("https://www.brandsmartusa.com/", ""));
			}
						
		}
		retVal = (String[]) lst.toArray(new String[lst.size()]);
		
		return retVal;
	}
	
	public String[] clickHeaderLinks() throws InterruptedException {
		
		//WebElement head=driver.findElement(By.xpath("(//ul[@class='list-inline list-unstyled top-bar text-right']//li//a)"));
		String[] val;
		List<String> list=new ArrayList<>();
		int length=driver.findElements(By.xpath("//ul[@class='list-inline list-unstyled top-bar text-right']//li//a")).size();
	
		for(int i=1; i<=length;i++) {
			driver.findElement(By.xpath("(//ul[@class='list-inline list-unstyled top-bar text-right']//li//a)["+i+"]")).click();
			Thread.sleep(1000);
			String txt=driver.getCurrentUrl();
			list.add(txt.replace("https://www.brandsmartusa.com/", ""));

		}
		val = (String[]) list.toArray(new String[list.size()]);

		return val;
	}
	public void clickOnStoreLocator() throws InterruptedException {
		storeLocator.click();
		Thread.sleep(1000);
		String element=driver.getTitle();
		System.out.println(element);
		System.out.println(florida.getText());
		
	}
	 	
	public String getStoreName() {
		return florida.getText();
	}
	public void clickLogin() {
		login.click();
		//logger.log(Status.PASS, "Login link clicked successfully.");
	}
	
	public void hoverOnL1Category(String l2Cat,String l3Cat) throws InterruptedException {
		WebElement l1Category=driver.findElement(By.xpath("//a[text()='Shop by ']"));
		WebElement l2Category=driver.findElement(By.xpath("//ul[@id='megaDropDown']//li//input[@aria-label='"+l2Cat +"']/../a"));
		WebElement l3Category=driver.findElement(By.xpath("//*[@id=\"departmentListSubTree0\"]//li//a[text()='"+l3Cat+"']"));
		
		
		Actions action = new Actions(driver);
		action.moveToElement(l1Category).build().perform();
		Thread.sleep(3000);
		action.moveToElement(l2Category).build().perform();
		Thread.sleep(1000);
		l3Category.click();
		//Utils.hoverAndSelect3(l1Category,l2Category,l3Category);		
	}
	
	public HomePage closeMemberPopUp() {
		closeMemberpopUp.click();
		return this;
	}
}

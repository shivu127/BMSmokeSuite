package com.bm.qa.pages;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utilities.Utils;

public class MyAccountPage {

	@FindBy(xpath = "//div[@class='row']//a[text()='Register']")
	WebElement RegistrationLink;

	@FindBy(xpath = "//*[@id='firstName']")
	WebElement FirstName;

	@FindBy(xpath = "//*[@id='lastName']")
	WebElement LastName;

	@FindBy(xpath = "//*[@id='emailReg']")
	WebElement EmailAddress;

	@FindBy(xpath = "//*[@id='passwordReg']")
	WebElement PasswordReg;

	@FindBy(xpath = "//*[@id='confirmPassword']")
	WebElement ConfirmPassword;

	@FindBy(xpath = "//form[@name='register']//button[@type='submit']")
	WebElement RegisterButton;

	@FindBy(xpath = "//a[@href='/myaccount/addressbook']")
	WebElement addressbookLink;

	@FindBy(xpath = "//button[text()='Add new address']")
	WebElement addNewAddressBtn;

	@FindBy(xpath = "//input[@name='firstName']")
	WebElement firstName;

	@FindBy(xpath = "//input[@name='lastName']")
	WebElement lastName;

	@FindBy(xpath = "//input[@name='address1']")
	WebElement addressLine1;

	@FindBy(xpath = "//input[@name='city']")
	WebElement cityName;

	@FindBy(xpath = "//input[@name='postalCode']")
	WebElement postalCode;

	@FindBy(xpath = "//input[@name='phoneNumber']")
	WebElement phoneNumber;

	@FindBy(xpath = "//input[@name='defaultShipping']")
	WebElement defaultShipping;

	@FindBy(xpath = "//input[@name='defaultBilling']")
	WebElement defaultBilling;

	@FindBy(xpath = "//button//span[text()='Add address']")
	WebElement addAddressBtn;

	@FindBy(xpath = "//button[text()='Cancel']")
	WebElement cancelBtn;

	@FindBy(xpath = "//button[@class='btn btnLinkBlue btn-block btn-gap']")
	WebElement deleteBtn;

	@FindBy(xpath = "//div[@class='bgTable']//a[@href='/wishlist']")
	WebElement wishlistLink;

	@FindBy(xpath = "//section[@class='wishlist']//a[@id='userDropDown']")
	WebElement shareWishlistBtn;

	@FindBy(xpath = "//section[@class='wishlist']/div[2]")
	WebElement emptyWishlist;
	
	@FindBy (xpath="//h1//strong['_ngcontent-c16']")
	WebElement AccountOverview;

	public WebDriver driver;
	public static ExtentTest logger;
	public static Utils utils;

	public MyAccountPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public boolean checkAccountOverviewText() throws Throwable {
		Thread.sleep(2000);
		return Utils.isDiplayedElement(AccountOverview, 3);
	}

	public MyAccountPage createAcc(String fName, String lName, String password) {

		int RN = (int) (Math.random() * 500);
		String emailID = "bms" + RN + "@gmail.com";
		System.out.println(emailID);
		FirstName.sendKeys(fName);
		LastName.sendKeys(lName);
		EmailAddress.sendKeys(emailID);
		PasswordReg.sendKeys(password);
		ConfirmPassword.sendKeys(password);
		RegisterButton.click();
		return this;
	}

	public MyAccountPage setAddress(String fName, String lName, String address1, String city, String zipCode,
			String phoneNumber, boolean enableShipping, boolean enableBilling) throws InterruptedException {
		firstName.sendKeys(fName);
		lastName.sendKeys(lName);
		addressLine1.sendKeys(address1);
		cityName.sendKeys(city);
		postalCode.sendKeys(zipCode);
		Thread.sleep(1000);
		this.phoneNumber.sendKeys(phoneNumber);
		if (enableShipping) {
			if (defaultShipping.isSelected()) {
				System.out.println("Check box already selected");
			} else {
				defaultShipping.click();
			}
		}

		if (enableBilling) {
			if (defaultBilling.isSelected()) {
				System.out.println("Check box already selected");
			} else {
				defaultBilling.click();
			}
		}
		return this;
	}

	public MyAccountPage clickCancelAddAddressBtn() {

		cancelBtn.click();
		logger.log(Status.PASS,"");
		return this;
	}

	public MyAccountPage clickAddAddressBtn() {

		addAddressBtn.click();
		return this;
	}

	public MyAccountPage clickOnAddressBookLink() throws InterruptedException {
		addressbookLink.click();
		Thread.sleep(2000);
		return this;
	}

	public MyAccountPage clickOnAddNewAddressBtn() {
		addNewAddressBtn.click();
		return this;
	}

	public boolean clickOnDeleteLink() throws InterruptedException {
		Thread.sleep(2000);
		deleteBtn.click();
		Thread.sleep(2000);
		WebElement element=driver.findElement(By.xpath("//div[@id='bodyContainer']/app-my-account/section[@class='container-fluid max-page-width my-account']//app-addressbook//app-global-msg/div[@class='alert alert-success']"));
		return utils.isDiplayedElement(element, 10);
		
	}

	public MyAccountPage deleteExistingAddress() throws InterruptedException {
		Thread.sleep(3000);
		try{
			boolean status1=driver.findElement(By.xpath("//button[text()='Delete']")).isDisplayed();
		    if(status1==false) {
		        System.out.println("Element is present and displayed");
				deleteBtn.click();
		    }
		    else {
		    	this.clickOnAddNewAddressBtn();
				this.setAddress("Shiva", "Gouda", "55 Broad St", "New York", "10004", "1211211211", false, false);
				this.clickAddAddressBtn();
		    }
		    Thread.sleep(1000);
			deleteBtn.click();
		        System.out.println("Element is present but not displayed"); 
		}catch (NoSuchElementException e) {
		    System.out.println("Element is not present, hence not displayed as well");
		}

		
		deleteBtn.click();

		return this;
		
	}

	public void clickOnWishlistLink() {
		wishlistLink.click();
	}

	public boolean isItemsAvailableInWishlist() throws InterruptedException {

		boolean status =null != null;
		WebElement element1 = driver.findElement(By.xpath("//section[@class='wishlist']/div[2]"));
		WebElement element2 = driver.findElement(By.xpath("//li[@class='row'][1]"));
		
		if (Utils.isDiplayedElement(element1, 3)) {
			Thread.sleep(1000);
			status = false;
		} else if (Utils.isDiplayedElement(element2, 3)) {
			Thread.sleep(1000);
			status = true;
		}
		return status;
	}

}

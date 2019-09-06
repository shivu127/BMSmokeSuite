package com.bm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;

public class MyAccPaymentPage {
	public WebDriver driver;
	public static ExtentTest logger;
	
	@FindBy (xpath="//input[@name='cardFirstName']")
	WebElement fName;
	
	@FindBy (xpath="//input[@name='cardLastName']")
	WebElement lName;
	
	@FindBy (xpath="//input[@name='creditCardNumber']")
	WebElement cardNumber;
	
	@FindBy (xpath="//select[@id='creditCardType']")
	WebElement creditCardType;
			
	@FindBy (xpath="//select[@id='expirationMonth']")
	WebElement expiryMonth;
		
	@FindBy (xpath="//select[@id='expirationYear']")
	WebElement expiryYear;
			
	@FindBy (xpath="//input[@name='defaultCreditCard']")
	WebElement makeThisDefault;
	
	@FindBy (xpath="//button[@class='btn btn-primary text-uppercase btn-block']")
	WebElement addPaymentButton;
	
	@FindBy (xpath="//button[@class='btn btn-style-2 btn-block']")
	WebElement cancelButton;
	
	@FindBy (xpath="//button[@class='btn btnLinkBlue btn-block']")
	WebElement editLink;
	
	@FindBy (xpath="//button[@class='btn btnLinkBlue btn-block btn-gap']")
	WebElement deleteLink;
	
	@FindBy (xpath="//button[@class='btn btn-primary btn-block text-uppercase btn-position']")
	WebElement addNewCard;
	
	@FindBy (xpath="//ul[@id='leftNavSection']//li//a[text()='Payments']")
	WebElement paymentsLink;
	
	@FindBy (xpath="//div[@id='bodyContainer']/app-my-account//app-payments//app-global-msg/div[@class='alert alert-success']")
	WebElement deleteMessage;
	
	@FindBy (xpath="//div[@class='row panelBorder']")
	WebElement addedCard;
	
	public MyAccPaymentPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public MyAccPaymentPage clickOnPaymentLink() {
			paymentsLink.click();	
			return this;
	}
	
	public MyAccPaymentPage clickOnAddNewCardBtn() throws Exception {
		Thread.sleep(2000);
		addNewCard.click();
		return this;
	}
	
	public MyAccPaymentPage enterFirsAndLastName(String FName,String LName ) {
		fName.sendKeys(FName);
		lName.sendKeys(LName);
		return this;
		
	}
	
	public MyAccPaymentPage selectCardType(String SelectcardType) {
		creditCardType.click();
		WebElement cardType=driver.findElement(By.xpath("//option[@value='"+SelectcardType+"']"));
		cardType.click();
		return this;
		
	}
	
	public MyAccPaymentPage enterCardNumber(String cardNumb) {
		cardNumber.sendKeys(cardNumb);
		return this;
		
	}
	
	public MyAccPaymentPage selectExpiryMonthAndYear(String expiryM,String expiryY) {
		WebElement optionExpiryMonth=driver.findElement(By.xpath("//option[@value='"+expiryM+"']"));
		WebElement selectExpiryYear=driver.findElement(By.xpath("//option[@value='"+expiryY+"']"));
		optionExpiryMonth.click();
		selectExpiryYear.click();
		
		return this;
		
	}
	
	public MyAccPaymentPage selectDefaultCard(boolean enableDefaultCard) {
		return this;
		
	}
	
	public MyAccPaymentPage clickOnAddPaymentBtn() {
		addPaymentButton.click();
		return this;
	}
	
}

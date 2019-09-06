package com.bm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class Login {

	WebDriver driver;
	
	@FindBy(xpath="/html//div[@id='collapseLogin']//form[@id='login-form']//input[@id='email']")
	WebElement username;
	
	@FindBy(xpath="/html//div[@id='collapseLogin']//form[@id='login-form']//input[@id='password']")
	WebElement password;
	
	@FindBy(xpath="//button[@class='btn btn-block btn-primary text-uppercase']/strong[.='Sign in now']")
	WebElement signin;
	
	@FindBy(xpath="//strong[.='Account Overview']")
	WebElement AccOverview;
	
	public Login(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}

	public Login enterUsername(String[] exp) {
		username.sendKeys(exp);
		return this;
	}
	
	public Login enterPwd(String[] pwd) {
		password.sendKeys(pwd);
		return this;
	}
	
	public Login clickSignBtn() {
		signin.click();
		return this;
	}
	
	public String getAccOverviewText() {
		return AccOverview.getText();
	}
	
}

package com.bm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage {
	public WebDriver driver;
	@FindBy (xpath="//input[@id='top-search']")
	WebElement searchTextBox;
	
	@FindBy (xpath="//button[@id='searchBtn']")
	WebElement searchBtn;
	
//	@FindAlBy (xpath="//div[@id='plist']//div[@class='row']")
//	WebElement searchedResultsCount;
//	
//	
	
	
	public SearchPage(WebDriver driver) {
	this.driver = driver;
	PageFactory.initElements(driver, this);
}
	
	public SearchPage enterSearchKeyword(String keyword) {
		searchTextBox.clear();
		searchTextBox.sendKeys(keyword);
		return this;
	}
	
	public SearchPage clickOnSearchBtn() {
		searchBtn.click();
		return this;
		
	}
	
	public boolean isSearchItemsDisplayed() throws InterruptedException {
		boolean status;
		Thread.sleep(2000);
		int resultsCount=driver.findElements(By.xpath("//div[@id='plist']//div[@class='row']")).size();
		if(resultsCount>0) {
			status=true;
		}else {
			status=false;
		}
		
		return status;
			
	}
}
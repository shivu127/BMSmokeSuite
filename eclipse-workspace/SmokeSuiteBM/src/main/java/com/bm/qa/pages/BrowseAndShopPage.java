package com.bm.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class BrowseAndShopPage {
	public WebDriver driver;
	
	@FindBy (xpath="")
	WebElement department;
	
	@FindBy (xpath="")
	WebElement categories;
	
	@FindBy (xpath="")
	WebElement subCategories;
	
	
/*	public void hoverOnL1Category(String l2Cat,String l3Cat) {
		WebElement l1Category=driver.findElement(By.xpath("//a[text()='Shop by ']"));
		WebElement l2Category=driver.findElement(By.xpath("//ul[@id='megaDropDown']//li//input[@aria-label=' "+l2Cat +"']"));
		WebElement l3Category=driver.findElement(By.xpath("//*[@id=\"departmentListSubTree0\"]//li//a[text()='"+l3Cat+"']"));
		Utils.hoverAndSelect3(l1Category,l2Category,l3Category);		
	}*/
	
	
}

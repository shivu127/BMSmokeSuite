package com.bm.qa.testcases;

import java.io.IOException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bm.qa.base.TestBase;
import com.bm.qa.pages.BrowseAndShopPage;
import com.bm.qa.pages.HomePage;
import com.bm.qa.pages.Login;
import com.bm.qa.pages.MyAccountPage;

public class BrowseAndShopTest extends TestBase{
	HomePage homePage;
	MyAccountPage registration;
	Login login;
	BrowseAndShopPage browseandshop;
	
	public BrowseAndShopTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}
	@BeforeMethod
	public void setup() throws IOException {

		initialisation();

		homePage = new HomePage(driver);
		registration = new MyAccountPage(driver);
		login = new Login(driver);
	}
	
	@Test
	public void TC_07() throws InterruptedException {
		homePage.hoverOnL1Category("Appliances & Housewares", "Kitchen Packages");
		Thread.sleep(2000);
		
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}

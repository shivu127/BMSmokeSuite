package com.bm.qa.testcases;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.bm.qa.base.TestBase;
import com.bm.qa.pages.HomePage;
import com.bm.qa.pages.Login;
import com.bm.qa.pages.MyAccPaymentPage;
import com.bm.qa.pages.MyAccountPage;
import java.lang.reflect.Method;
import utilities.ExcelUtils;
import utilities.Utils;

public class AccountManagementTest extends TestBase {


	MyAccountPage myAcc;
	HomePage homePage;
	Login login;
	Utils utils;
	MyAccPaymentPage MyAccPay;


	public AccountManagementTest() throws IOException {
		super();
	}

	@BeforeMethod
	public void setup(Method method) throws IOException {
		initialisation();
		homePage = new HomePage(driver);
		myAcc = new MyAccountPage(driver);
		login = new Login(driver);
		MyAccPay=new MyAccPaymentPage(driver);
		utils = new Utils();
		logger=extent.createTest(method.getName(), method.getAnnotation(Test.class).testName())
		.assignCategory(this.getClass().getSimpleName()).assignCategory("Smoke Test");

	}
	
	@Test(testName = "To verify that user is able to add/delete the Address", enabled = true)
	public void TC_03_1() throws Throwable {
						
			homePage.closeMemberPopUp().clickLogin();
			logger.createNode("Clicked login link successfully");
			String[] uName = ExcelUtils.getExcelDataByColumn("credentials", 1);
			String[] pwd = ExcelUtils.getExcelDataByColumn("credentials", 2);
			login.enterUsername(uName).enterPwd(pwd).clickSignBtn();
			Assert.assertTrue(myAcc.checkAccountOverviewText());
			logger.createNode("User is on My account overview page");
			Thread.sleep(2000);
			myAcc.clickOnAddressBookLink();
			myAcc.clickOnAddNewAddressBtn();
			myAcc.setAddress("Shiva", "Gouda", "55 Broad St", "New York", "10004", "1211211211", false, false)
					.clickAddAddressBtn();
			logger.createNode("Address Added successfully");
			//utils.commFunc.waitForPageLoaded();
			Thread.sleep(2000);
			myAcc.clickOnDeleteLink();
			Thread.sleep(2000);
			Assert.assertTrue(myAcc.clickOnDeleteLink());
			logger.createNode("Address deleted successfully");
			//utils.commFunc.getAlertText(driver);
	}
	
	@Test(testName = "To verify the functionality of the store locator", enabled = true)
	public void TC_04 () throws Throwable {
		homePage.closeMemberPopUp().clickLogin();
		String[] uName = ExcelUtils.getExcelDataByColumn("credentials", 1);
		String[] pwd = ExcelUtils.getExcelDataByColumn("credentials", 2);
		login.enterUsername(uName).enterPwd(pwd).clickSignBtn();
		Assert.assertTrue(myAcc.checkAccountOverviewText());
		logger.createNode("User signed in successfully");
		myAcc.clickOnWishlistLink();

	}

	@Test(testName="To verify that user is able to add new Payment", enabled=true)
	public void TC_03() throws Throwable {
		homePage.closeMemberPopUp().clickLogin();
		String[] uName = ExcelUtils.getExcelDataByColumn("credentials", 1);
		String[] pwd = ExcelUtils.getExcelDataByColumn("credentials", 2);
		login.enterUsername(uName).enterPwd(pwd).clickSignBtn();
		Assert.assertTrue(myAcc.checkAccountOverviewText());
		logger.createNode("User signed in successfully");
		MyAccPay.clickOnPaymentLink().clickOnAddNewCardBtn().selectCardType("visa").enterCardNumber("4200000000000000")
		.enterFirsAndLastName("Shiva", "Gouda").selectExpiryMonthAndYear("2", "2020").clickOnAddPaymentBtn();
		
	}

}

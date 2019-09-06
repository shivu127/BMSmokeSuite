package com.bm.qa.testcases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.bm.qa.base.BasePage;
import com.bm.qa.base.TestBase;
import com.bm.qa.pages.HomePage;
import com.bm.qa.pages.Login;
import com.bm.qa.pages.MyAccountPage;
import utilities.ExcelUtils;

public class HomePageTest extends TestBase {
	public HomePageTest() throws IOException {
		super();
	}

	HomePage homepage;
	MyAccountPage register;
	Login login;

	@BeforeMethod
	public void setup(Method method) throws IOException {
		initialisation();
		homepage = new HomePage(driver);
		register = new MyAccountPage(driver);
		login = new Login(driver);
		logger=extent.createTest(method.getName(), method.getAnnotation(Test.class).testName())
		.assignCategory(this.getClass().getSimpleName()).assignCategory("SmokeTest");
	}


	@Test(testName = "Verify user is able to access home page and verify the contents and links", enabled = true)
	public void TC_08() throws InterruptedException, IOException {
		homepage.closeMemberPopUp();
		String[] expected = ExcelUtils.getExcelDataByColumn("Footerlinks", 1);
		String[] actual = homepage.clickOnFooterLinks();
		Assert.assertEquals(actual, expected);
		logger.log(Status.INFO, "Footer links working properly");
		String[] exp = ExcelUtils.getExcelDataByColumn("Headerlinks", 1);
		String[] act = homepage.clickHeaderLinks();
		Assert.assertEquals(act, exp);
		logger.log(Status.INFO, "Header links working properly");

	}

	@Test(testName = "Verify that user can find stores in different locations", enabled = true)
	public void TC_15() throws InterruptedException {
		homepage.closeMemberPopUp().clickOnStoreLocator();
		Assert.assertEquals(homepage.getStoreName(), "Florida Stores");
		logger.createNode("Store locator clicked and verified");

	}

	@Test(testName = "To verify the registration from header link.", enabled = true)
	public void TC_01() throws Exception {
		
		homepage.closeMemberPopUp().clickOnRegisterLink();
		register.createAcc("Shiva", "gouda", "Test@123");
	}

	@Test(testName = "Verify that user is able to login from header section", enabled = true)
	public void TC_02() {
		String[] uName = ExcelUtils.getExcelDataByColumn("credentials", 1);
		String[] pwd = ExcelUtils.getExcelDataByColumn("credentials", 2);
		homepage.closeMemberPopUp().clickLogin();
		login.enterUsername(uName).enterPwd(pwd).clickSignBtn();
		Assert.assertEquals(login.getAccOverviewText(), "Account Overview");
	}

}

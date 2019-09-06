package com.bm.qa.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bm.qa.base.TestBase;
import com.bm.qa.pages.HomePage;
import com.bm.qa.pages.Login;
import com.bm.qa.pages.MyAccountPage;
import com.bm.qa.pages.SearchPage;

import utilities.ExcelUtils;

public class searchTest extends TestBase {
	HomePage home;
	SearchPage search;

	public searchTest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@BeforeMethod
	public void setup() throws IOException {

		initialisation();

		home = new HomePage(driver);
		search = new SearchPage(driver);
	}

	@Test
	public void TC_12() throws InterruptedException {

		String[] keyword = ExcelUtils.getExcelDataByColumn("search", 1);
		for (int i = 0; i <= keyword.length-1; i++) {
			search.enterSearchKeyword(keyword[i]).clickOnSearchBtn();
			Thread.sleep(2000);
			Assert.assertEquals(search.isSearchItemsDisplayed(), true);
		}

	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}

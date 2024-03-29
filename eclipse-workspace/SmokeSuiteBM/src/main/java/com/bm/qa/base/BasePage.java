package com.bm.qa.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class BasePage 
{
	public static WebDriver driver;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;
	public static Properties prop;


	public BasePage() throws IOException {

		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\java\\com\\bm\\qa\\config\\config.properties");
			prop.load(ip);
			System.out.println("ip:" + prop.getProperty("url"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initialisation() {

		String browserName = prop.getProperty("Browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
			driver = new ChromeDriver();
		} else {
			System.setProperty("webdriver.geko.driver", "");
			driver = new ChromeDriver();
		}
		driver.get(prop.getProperty("url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	@BeforeSuite
	public ExtentReports extentReportSetup() {
		//location of the extent report
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		extent = new ExtentReports();  //create object of ExtentReports
		extent.attachReporter(htmlReporter);

		htmlReporter.config().setDocumentTitle("Automation Report"); // Tittle of Report
		htmlReporter.config().setReportName("Extent Report V4"); // Name of the report
		htmlReporter.config().setTheme(Theme.DARK);//Default Theme of Report

		// General information releated to application
		extent.setSystemInfo("Application Name", "Google Test");
		extent.setSystemInfo("User Name", "Ankur Jain");
		extent.setSystemInfo("Envirnoment", "Production");
		return extent;
	}

	@AfterSuite
	public void endReport() {
		extent.flush();
	}

//	@BeforeMethod
//	public void setup() {
//		System.setProperty("webdriver.chrome.driver","C:\\Users\\Admin\\eclipse-workspace\\extent\\drivers\\chromedriver.exe");
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get("http://www.google.com/");
//	}

/*	@Test
	public void GoogleTittleValidation() {
		test = extent.createTest("GoogleTittleValidation");
		Assert.assertEquals(driver.getTitle(),"Google");
	}

	@Test
	public void GoogleLogoValidation() {
		test = extent.createTest("GoogleLogoValidation");
		boolean img = driver.findElement(By.xpath("//img[@id='hplogo']")).isDisplayed();
		test.createNode("ImagePresentYes");
		Assert.assertTrue(img);
		test.createNode("ImagePresentNo");
		Assert.assertFalse(img);
	}*/

	@AfterMethod
	public void getResult(ITestResult result) throws Exception
	{
		if(result.getStatus() == ITestResult.FAILURE)
		{
			//MarkupHelper is used to display the output in different colors
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));

			//To capture screenshot path and store the path of the screenshot in the string "screenshotPath"
			//We do pass the path captured by this method in to the extent reports using "logger.addScreenCapture" method. 
			//	String Scrnshot=TakeScreenshot.captuerScreenshot(driver,"TestCaseFailed");
			String screenshotPath = TakeScreenshot(driver, result.getName());
			//To add it in the extent report 
			logger.fail("Test Case Failed Snapshot is below " + logger.addScreenCaptureFromPath(screenshotPath));
		}
		else if(result.getStatus() == ITestResult.SKIP){
			//logger.log(Status.SKIP, "Test Case Skipped is "+result.getName());
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE)); 
		} 
		else if(result.getStatus() == ITestResult.SUCCESS)
		{
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
		}
		driver.quit();
	}

	public static String TakeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		// after execution, you could see a folder "FailedTestsScreenshots" under src folder
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
}

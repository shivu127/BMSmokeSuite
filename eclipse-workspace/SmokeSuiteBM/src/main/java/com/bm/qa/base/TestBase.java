package com.bm.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestBase {

	public static WebDriver driver;
	public static Properties prop;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public static ExtentHtmlReporter htmlReporter;
	
	@BeforeSuite
	public static ExtentReports setUpSuite() {
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(
				"C:\\Users\\Admin\\eclipse-workspace\\SmokeSuiteBM\\reports\\Ddemo.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", Platform.getCurrent().toString());
		extent.setSystemInfo("Host Name", "AI Enterprise");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
		return extent;
	}

	@AfterMethod
	public void captureStatus(ITestResult result) throws Exception {

		System.out.println(("*** Running test method " + result.getName() + "..."));

		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL, MarkupHelper.createLabel("FAILED", ExtentColor.RED));
			logger.fail(MarkupHelper.createLabel(result.getThrowable()+"-Test Case Failed",ExtentColor.RED));
			String imagePath=TakeScreenshot(driver,result.getName());
			logger.fail("Failed due to image below: "+logger.addScreenCaptureFromPath(imagePath));
			logger.info("Screenshot Path-"+imagePath);
			System.out.println("*** Test " + result.getName() + " Failed...");

		} else if ((result.getStatus() == ITestResult.SUCCESS)) {
			logger.log(Status.PASS, MarkupHelper.createLabel("PASSED", ExtentColor.GREEN));
			System.out.println("*** Executed " + result.getName() + " test successfully...");

		} else {
			logger.log(Status.SKIP, MarkupHelper.createLabel("SKIPPED", ExtentColor.ORANGE))
					.skip(result.getThrowable());

			System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
		}

		driver.quit();
	}

	@AfterSuite
	public void tearDown() {
		extent.flush();
		zipExtentReport();
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

	public static void zipExtentReport() {
		try {

			String filePath = System.getProperty("user.dir") + "\\reports\\";
			File file = new File(filePath + "Ddemo.html");

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			formater.format(calendar.getTime());
			String zipFileName = filePath + "archives\\" + "Ddemo.html_" + formater.format(calendar.getTime()) + ".zip";

			addToZipFile(file, zipFileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void addToZipFile(File file, String zipFileName) throws FileNotFoundException, IOException {

		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);

		ZipEntry zipEntry = new ZipEntry(file.getName());
		zos.putNextEntry(zipEntry);

		FileInputStream fis = new FileInputStream(file);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) > 0) {
			zos.write(bytes, 0, length);
		}
		zos.closeEntry();
		zos.close();
		fis.close();
		fos.close();
		System.out.println(file.getCanonicalPath() + " is zipped to " + zipFileName);
	}

	public TestBase() throws IOException {

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
}

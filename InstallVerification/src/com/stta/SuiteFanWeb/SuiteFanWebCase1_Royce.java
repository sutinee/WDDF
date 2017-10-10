package com.stta.SuiteFanWeb;



import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.stta.utility.Read_XLS;
import com.stta.utility.SuiteUtility;

//SuiteOneCaseOne Class Inherits From SuiteOneBase Class.
//So, SuiteOneCaseOne Class Is Child Class Of SuiteOneBase Class And SuiteBase Class.
public class SuiteFanWebCase1_Royce extends SuiteFanWebBase{
	Read_XLS FilePath = null;
	String SheetName = null;
	String TestCaseName = null;	
	String ToRunColumnNameTestCase = null;
	String ToRunColumnNameTestData = null;
	String TestDataToRun[]=null;
	static boolean TestCasePass=true;
	static int DataSet=-1;	
	static boolean Testskip=false;
	static boolean Testfail=false;
	SoftAssert s_assert =null;	
	
	@BeforeTest
	public void checkCaseToRun() throws IOException{
		init();			
		//To set SuiteOne.xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelFanWeb;		
		TestCaseName = this.getClass().getSimpleName();	
		//SheetName to check CaseToRun flag against test case.
		SheetName = "TestCasesList";
		//Name of column In TestCasesList Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		//Name of column In Test Case Data sheets.
		ToRunColumnNameTestData = "DataToRun";
		//Bellow given syntax will Insert log In applog.log file.
		Add_Log.info(TestCaseName+" : Execution started.");
		
		//To check test case's CaseToRun = Y or N In related excel sheet.
		//If CaseToRun = N or blank, Test case will skip execution. Else It will be executed.
		if(!SuiteUtility.checkToRunUtility(FilePath, SheetName,ToRunColumnNameTestCase,TestCaseName)){
			Add_Log.info(TestCaseName+" : CaseToRun = N for So Skipping Execution.");
			//To report result as skip for test cases In TestCasesList sheet.
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "SKIP");
			//To throw skip exception for this test case.
			throw new SkipException(TestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+TestCaseName);
		}	
		//To retrieve DataToRun flags of all data set lines from related test data sheet.
		TestDataToRun = SuiteUtility.checkToRunUtilityOfData(FilePath, TestCaseName, ToRunColumnNameTestData);
	}
	
	//Accepts 4 column's String data In every Iteration.
	@Test(dataProvider="SuiteFanWebCase1_Royce")
	public void SuiteFanWebCase1_RoyceTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword) throws InterruptedException, IOException, UnsupportedFlavorException{
		
		DataSet++;
		
		//Created object of testng SoftAssert class.
		s_assert = new SoftAssert();
		String fileName_confirmation_statement = "";
		String fileName_periodic_statement = "";
		
		//If found DataToRun = "N" for data set then execution will be skipped for that data set.
		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
			Add_Log.info(TestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
			//If DataToRun = "N", Set Testskip=true.
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}
		Reporter.log("<br>Screenshots of " + TestCaseName);
		loadWebBrowser(DataBrowser);		
		driver.get(DataURL);
		//userLoginProxy();
		userLoginFanWeb(DataUsername, DataPassword);
		Add_Log.info("Logged in");
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		try {
			  Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
			  Thread.currentThread().interrupt();
			}
		//Click the "drop down" on the right side of the screen that is defaulted to Portfolio Options
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[2]/div/aside/div/div[1]")));
		driver.findElement(By.xpath("html/body/div[2]/div/aside/div/div[1]")).click();
		Add_Log.info("Clicked dropdown Portfolio Options");
		Thread.sleep(300);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[2]/div/aside/div/div[2]/ul/li[3]/a")));
		driver.findElement(By.xpath("html/body/div[2]/div/aside/div/div[2]/ul/li[3]/a")).click();
		Add_Log.info("Selected Confirmation Statements");
		
		//Click the "August 7, 2015 Statement" link
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[2]/div/article")));
		String statementList = driver.findElement(By.xpath("html/body/div[2]/div/article")).getText();
		if(!statementList.contains("Statement"))
		{
			s_assert.assertTrue(statementList.contains("Statement"), "Wording NOT FOUND: Statement");
			Add_Log.info("Wording NOT FOUND: Statement");
		    Testfail = true;
		}
		else
		{
			Add_Log.info("Statement Lists are displayed. Click on one of the statement");
		}
			// Click one of the Confirmation Statement
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='alinks']")));
			driver.findElement(By.xpath(".//*[@id='alinks']")).click();
			try {
				Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			String currentURL = driver.getCurrentUrl();
			if(currentURL.contains(".financialtrans")) // current URL on Chrome and FF are the same.
			{
				// Verify PDF contents
				/*wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body")));
				driver.findElement(By.xpath("//body")).click();
				Thread.sleep(clipboardTime);
				driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				Thread.sleep(clipboardTime);
				driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
				Thread.sleep(clipboardTime);
				driver.findElement(By.xpath("//body")).click();
				Thread.sleep(clipboardTime);
				String PDFContent = getClipBoard();
				Thread.sleep(clipboardTime);
				//Add_Log.info("PDFContent" + PDFContent);
				Add_Log.info("PDF can be opened. As expected");
				if(PDFContent.contains("Confirmation Statement"))
				{
				   	fileName_confirmation_statement = takeScreenshot(TestCaseName, "1_View_ConfirmationStatement");
					Add_Log.info("PDF's content is correct. As expected");
				}
				else
				{
				   	Add_Log.info("PDF content does not contain expected wording: Confirmation Statement");
				    Testfail = true;
				    s_assert.assertTrue(PDFContent.contains("Confirmation Statement"), "PDF content does not contain expected wording: Confirmation Statement");
				    fileName_confirmation_statement = takeScreenshot(TestCaseName, "1_View_ConfirmationStatementFail");
				}*/
				String PDFContent = "";
				String expectedWording = "Confirmation Statement";
				PDFContent = verifyPDFContent();
				// In case PDF is taking long time to load. Wait x2 times and get clip board again
				if(!PDFContent.contains(expectedWording))
				{
					try {
						Thread.sleep(waitFileLoad);
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					PDFContent = verifyPDFContent();
				}
				if(PDFContent.contains(expectedWording))
				{
					fileName_confirmation_statement = takeScreenshot(TestCaseName, "1_View_ConfirmationStatement");
					Add_Log.info("PDF's content is correct. As expected");
				}
				else
				{
					Add_Log.info("PDF content does not contain expected wording:" + expectedWording);
					Testfail = true;
					s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected wording: " + expectedWording);
					fileName_confirmation_statement = takeScreenshot(TestCaseName, "1_View_ConfirmationStatementFail");
				}	
					
			}
			else
			{
				Add_Log.info("FAIL: Error occurs upon opening Statement");
				Testfail = true;
				s_assert.assertTrue(driver.getCurrentUrl().contains(".financialtrans"), "Error occurs upon opening Statement");
				fileName_confirmation_statement = takeScreenshot(TestCaseName, "1_View_ConfirmationStatementFail");
			}
			//Click "Return to Confirmation Statements" button => NOT AVAILABLE. Use browser's Back button
			driver.navigate().back();
			Add_Log.info("Clickd browser Back button.");
			//Click "Return to Portfolio Summary" button
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[2]/div/article/form/p/input")));
			driver.findElement(By.xpath("html/body/div[2]/div/article/form/p/input")).click();
			//===================================================================================================================================================================
			//Click the "drop down" on the right side of the screen that is defaulted to Portfolio Options
			try {
				  Thread.sleep(waitFileLoad);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[2]/div/aside/div/div[1]")));
			driver.findElement(By.xpath("html/body/div[2]/div/aside/div/div[1]")).click();
			Add_Log.info("Clicked dropdown Portfolio Options");
			Thread.sleep(300);
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/div[2]/div/aside/div[1]/div[2]/ul/li[4]/a")));
			driver.findElement(By.xpath("html/body/div[2]/div/aside/div[1]/div[2]/ul/li[4]/a")).click();
			Add_Log.info("Clicked Periodic Statements");
			//Click the "Statement" link
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[2]/div/article")));
			statementList = driver.findElement(By.xpath("html/body/div[2]/div/article")).getText();
			if(!statementList.contains("Statement"))
			{
				s_assert.assertTrue(statementList.contains("Statement"), "Wording NOT FOUND: Statement");
				Add_Log.info("Wording NOT FOUND: Statement");
	        	Testfail = true;
			}
			else
			{
				Add_Log.info("Statement Lists are displayed. Click on one of the statement");
			}
			driver.findElement(By.xpath(".//*[@id='alinks']")).click();
			try {
				  Thread.sleep(waitFileLoad);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			currentURL = driver.getCurrentUrl();
			if(currentURL.contains(".financialtrans")) // current URL on Chrome and FF are the same.
			{
				// Verify PDF contents
				/*wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//body")));
				driver.findElement(By.xpath("//body")).click();
				driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
				Thread.sleep(clipboardTime);
			    driver.findElement(By.xpath("//body")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
			    Thread.sleep(clipboardTime);
			    driver.findElement(By.xpath("//body")).click();
			    Thread.sleep(clipboardTime);
			    String PDFContent = getClipBoard();
			    Add_Log.info("PDF can be opened. As expected");
			    if(PDFContent.contains("CONSOLIDATED STATEMENT"))
			    {
			    	fileName_periodic_statement = takeScreenshot(TestCaseName, "1_View_PeriodicStatement");
					Add_Log.info("PDF's content is correct. As expected");
			    }
			    else
			    {
			    	Add_Log.info("PDF content does not contain expected wording: CONSOLIDATED STATEMENT");
			    	Testfail = true;
			    	s_assert.assertTrue(PDFContent.contains("CONSOLIDATED STATEMENT"), "PDF content does not contain expected wording: CONSOLIDATED STATEMENT");
			    	fileName_periodic_statement = takeScreenshot(TestCaseName, "1_View_PeriodicStatementFail");
			    }*/
				String PDFContent = "";
				String expectedWording = "CONSOLIDATED STATEMENT";
				PDFContent = verifyPDFContent();
				// In case PDF is taking long time to load. Wait x2 times and get clip board again
				if(!PDFContent.contains(expectedWording))
				{
					try {
						Thread.sleep(waitFileLoad);
					} catch(InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
					PDFContent = verifyPDFContent();
				}
			if(PDFContent.contains(expectedWording))
				{
					fileName_periodic_statement = takeScreenshot(TestCaseName, "1_View_PeriodicStatement");
					Add_Log.info("PDF's content is correct. As expected");
				}
				else
				{
					Add_Log.info("PDF content does not contain expected wording:" + expectedWording);
					Testfail = true;
					s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected wording: " + expectedWording);
					fileName_periodic_statement = takeScreenshot(TestCaseName, "1_View_PeriodicStatementFail");
				}	
			}
			else
			{
				Add_Log.info("FAIL: Error occurs upon opening Statement");
				Testfail = true;
				s_assert.assertTrue(driver.getCurrentUrl().contains(".financialtrans"), "Error occurs upon opening Statement");
				fileName_periodic_statement = takeScreenshot(TestCaseName, "1_View_PeriodicStatementFail");
			}
			
			//Click browser's Back button
			driver.navigate().back();
			Add_Log.info("Clicked browser's Back button");
			
			//Click on the "Account Access Log Out" button in the upper right corner
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='log-out']/span")));
			driver.findElement(By.xpath(".//*[@id='log-out']/span")).click();
					
			Add_Log.info("Clicked on the 'Account Access Log Out' button");
			
			// Adding screenshot to report =======================================================================================================================
			reportOutput(fileName_confirmation_statement, "1. View Confirmation Statement", "1. Error occurs on Confirmation Statement file");
			reportOutput(fileName_periodic_statement, "2. View Periodic Statement", "2. Error occurs on Periodic Statement file");
			//=======================================================================================================================================================
			if(Testfail){
				//At last, test data assertion failure will be reported In testNG reports and It will mark your test data, test case and test suite as fail.
				s_assert.assertAll();		
			}
		
		

	}
	
	//@AfterMethod method will be executed after execution of @Test method every time.
	@AfterMethod
	public void reporterDataResults(){		
		if(Testskip){
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as SKIP In excel.");
			//If found Testskip = true, Result will be reported as SKIP against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "SKIP");
		}
		else if(Testfail){
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as FAIL In excel.");
			//To make object reference null after reporting In report.
			s_assert = null;
			//Set TestCasePass = false to report test case as fail In excel sheet.
			TestCasePass=false;	
			//If found Testfail = true, Result will be reported as FAIL against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "FAIL");			
		}else{
			Add_Log.info(TestCaseName+" : Reporting test data set line "+(DataSet+1)+" as PASS In excel.");
			//If found Testskip = false and Testfail = false, Result will be reported as PASS against data set line In excel sheet.
			SuiteUtility.WriteResultUtility(FilePath, TestCaseName, "Pass/Fail/Skip", DataSet+1, "PASS");
		}
		//At last make both flags as false for next data set.
		Testskip=false;
		Testfail=false;
	}
	
	//This data provider method will return 4 column's data one by one In every Iteration.
	@DataProvider
	public Object[][] SuiteFanWebCase1_Royce(){
		//To retrieve data from Data 1 Column,Data 2 Column,Data 3 Column and Expected Result column of SuiteOneCaseOne data Sheet.
		//Last two columns (DataToRun and Pass/Fail/Skip) are Ignored programatically when reading test data.
		return SuiteUtility.GetTestDataUtility(FilePath, TestCaseName);
	}	
	
	//To report result as pass or fail for test cases In TestCasesList sheet.
	@AfterTest
	public void closeBrowser(){
		//To Close the web browser at the end of test.
		closeWebBrowser();
		if(TestCasePass){
			Add_Log.info(TestCaseName+" : Reporting test case as PASS In excel.");
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "PASS");
		}
		else{
			Add_Log.info(TestCaseName+" : Reporting test case as FAIL In excel.");
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "FAIL");			
		}
	}
}
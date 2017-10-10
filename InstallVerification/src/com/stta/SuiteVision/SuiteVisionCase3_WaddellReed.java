package com.stta.SuiteVision;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
public class SuiteVisionCase3_WaddellReed extends SuiteVisionBase{
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
		FilePath = TestCaseListExcelVision;		
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
	
	@Test(dataProvider="SuiteVisionCase3_WaddellReedData")
	public void SuiteVisionCase3_WaddellReedTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword, String DataSSN, String mostRecentStatement) throws InterruptedException, IOException, UnsupportedFlavorException{
		
		DataSet++;
				//Created object of testng SoftAssert class.
		s_assert = new SoftAssert();
		String fileName1_statement = "";
		String fileName2_statement = "";
		String fileName3_statement = "";
		String accountNo = "35010498";
		// statementNo1 is most recent one (default)
		String selectStatementNo2 = "INVESTOR 12/31/2008";
		String selectStatementNo3 = "INVESTOR 12/31/2005";

		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
			Add_Log.info(TestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}
		Reporter.log("<br>Screenshots of " + TestCaseName);
		loadWebBrowser(DataBrowser);		
		driver.get(DataURL);
		userLoginVision(DataUsername, DataPassword);
		accessSSNPage("W", "WADDELL & REED/IVY FUNDS", DataSSN);
		boolean accountExist = checkAccount(accountNo);
		
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		if(!accountExist)
		{
			s_assert.assertTrue(accountExist, "Cannot find account #"+ accountNo +" on Tax ID/SSN page");
			Add_Log.info("Cannot find account #"+ accountNo +" on Tax ID/SSN page");
        	Testfail = true;
		}else
		{
			//Search Account Number using "Account Number" link
			Add_Log.info("Account Found #" + accountNo);
			searchAccount(accountNo);

			//Select 'Statement' link on the left navigation bar
			
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Statements")));
			driver.findElement(By.linkText("Statements")).click();
			Add_Log.info("Clicked 'Statements' on the left navigation bar");
			Thread.sleep(5000);
			
			// The most recent statement will display
			driver.switchTo().defaultContent(); // you are now outside both frames
			driver.switchTo().frame("viewstmt");
			driver.switchTo().frame("navframe");
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("idx")));
			Select dropdownDoc = new Select(driver.findElement(By.id("idx")));
			String selectedOption = dropdownDoc.getFirstSelectedOption().getText();
			selectedOption = selectedOption.trim();
			Add_Log.info("The most recent statement is : "+ selectedOption);
		
			if(!selectedOption.contains(mostRecentStatement))
			{
				s_assert.assertEquals(selectedOption, mostRecentStatement, "The most recent statement is NOT display");
				Add_Log.info("The most recent statement is NOT displayed. Expected: "+ mostRecentStatement);
	        	Testfail = true;
			}
			else
			{
				Add_Log.info("The most recent statement is displayed. As Expected: "+ mostRecentStatement);
				// Verify PDF contents
				try {
					 // Thread.sleep(waitFileLoad);
					Thread.sleep(10000);
					} catch(InterruptedException ex) {
					  Thread.currentThread().interrupt();
					}
				driver.switchTo().defaultContent(); // you are now outside both frames
				driver.switchTo().frame("viewstmt");
				driver.switchTo().frame("stmtframe");
				
				String PDFContent = "";
				String expectedWording = "Account";
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
					fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatement");
					Add_Log.info("PDF's content is correct. As expected");
				}
				else
				{

					Add_Log.info("PDF content does not contain expected wording:" + expectedWording);
					Testfail = true;
					s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected wording: " + expectedWording);
					fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatementFail");
				}	// (most recent statement)

			} // end if verify most recent
			//================Click the dropdown and select 'INVESTOR 12/31/2007' from the list ========================================
			driver.switchTo().defaultContent(); 
			driver.switchTo().frame("viewstmt");
			driver.switchTo().frame("navframe");
			dropdownDoc.selectByVisibleText(selectStatementNo2);
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Retrieve Statement")));
			driver.findElement(By.name("Retrieve Statement")).click();
			Add_Log.info("Clicked the dropdown and select 'INVESTOR 12/31/2007' from the list");
			
			// Verify PDF contents
			try {
				  Thread.sleep(waitFileLoad);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			driver.switchTo().defaultContent(); // you are now outside both frames
			driver.switchTo().frame("viewstmt");
			driver.switchTo().frame("stmtframe");
			
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
			//Add_Log.info("PDFContent" + PDFContent);
			Add_Log.info("PDF can be opened. As expected");
			if(PDFContent.contains("Account"))
			{
				  fileName2_statement = takeScreenshot(TestCaseName, "2_ViewStatement");
				  Add_Log.info("PDF's content is correct. As expected");
			}
			else
			{
				  Add_Log.info("PDF content does not contain expected wording : Account");
				  Testfail = true;
				  s_assert.assertTrue(PDFContent.contains("Account"), "PDF content does not contain expected wording : Account");
				  fileName2_statement = takeScreenshot(TestCaseName, "2_ViewStatementFail");
			}*/
			String PDFContent = "";
			String expectedWording = "Account";
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
				fileName2_statement = takeScreenshot(TestCaseName, "2_ViewStatement");
				Add_Log.info("PDF's content is correct. As expected");
			}
			else
			{

				Add_Log.info("PDF content does not contain expected wording:" + expectedWording);
				Testfail = true;
				s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected wording: " + expectedWording);
				fileName2_statement = takeScreenshot(TestCaseName, "2_ViewStatementFail");
			}	
			//================Click the dropdown and select 'INVESTOR 12/31/2007' from the list ========================================
			driver.switchTo().defaultContent(); 
			driver.switchTo().frame("viewstmt");
			driver.switchTo().frame("navframe");
			dropdownDoc.selectByVisibleText(selectStatementNo3);
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Retrieve Statement")));
			driver.findElement(By.name("Retrieve Statement")).click();
			Add_Log.info("Clicked the dropdown and select 'INVESTOR 12/31/2005' from the list");
			
			// Verify PDF contents
			try {
				  Thread.sleep(waitFileLoad);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			driver.switchTo().defaultContent(); // you are now outside both frames
			driver.switchTo().frame("viewstmt");
			driver.switchTo().frame("stmtframe");
			
			PDFContent = "";
			expectedWording = "Account";
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
				fileName3_statement = takeScreenshot(TestCaseName, "3_ViewStatement");
				Add_Log.info("PDF's content is correct. As expected");
			}
			else
			{

				Add_Log.info("PDF content does not contain expected wording:" + expectedWording);
				Testfail = true;
				s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected wording: " + expectedWording);
				fileName3_statement = takeScreenshot(TestCaseName, "3_ViewStatementFail");
			}	
			
		} // end account exist
				
		//Clicked SignOut at the top of the screen
		driver.switchTo().defaultContent(); // you are now outside both frames
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='Sign Out']")));
		driver.findElement(By.xpath(".//*[@id='Sign Out']")).click();
		Add_Log.info("Signed out");
		
		// Adding screenshot to report =======================================================================================================================
		reportOutput(fileName1_statement, "1. View statement file", "1. Error occurs upon opening statement file");
		reportOutput(fileName2_statement, "2. View statement file", "2. Error occurs upon opening statement file");
		reportOutput(fileName3_statement, "3. View statement file", "3. Error occurs upon opening statement file");
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
	public Object[][] SuiteVisionCase3_WaddellReedData(){
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
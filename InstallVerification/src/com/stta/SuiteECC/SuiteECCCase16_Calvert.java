package com.stta.SuiteECC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.openqa.selenium.By;
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

public class SuiteECCCase16_Calvert extends SuiteECCBase{
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
		//To set .xls file's path In FilePath Variable.
		FilePath = TestCaseListExcelECC;		
		TestCaseName = this.getClass().getSimpleName();	
		//SheetName to check CaseToRun flag against test case.
		SheetName = "TestCasesList";
		//Name of column In TestCasesList Excel sheet.
		ToRunColumnNameTestCase = "CaseToRun";
		//Name of column In Test Case Data sheets.
		ToRunColumnNameTestData = "DataToRun";
		Add_Log.info(TestCaseName+" : Execution started.");
		
		if(!SuiteUtility.checkToRunUtility(FilePath, SheetName,ToRunColumnNameTestCase,TestCaseName)){
			Add_Log.info(TestCaseName+" : CaseToRun = N for So Skipping Execution.");
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Pass/Fail/Skip", TestCaseName, "SKIP");
			throw new SkipException(TestCaseName+"'s CaseToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+TestCaseName);
		}	
		//To retrieve DataToRun flags of all data set lines from related test data sheet.
		TestDataToRun = SuiteUtility.checkToRunUtilityOfData(FilePath, TestCaseName, ToRunColumnNameTestData);
	}
	
	@Test(dataProvider="SuiteECCCase16_CalvertData")
	public void SuiteECCCase16_CalvertDataTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword, String DataTaxID, String DataBeginDate) throws InterruptedException, IOException{
		
		DataSet++;
		s_assert = new SoftAssert();
		String fileName1_statement = "";

		if(!TestDataToRun[DataSet].equalsIgnoreCase("Y")){	
			Add_Log.info(TestCaseName+" : DataToRun = N for data set line "+(DataSet+1)+" So skipping Its execution.");
			Testskip=true;
			throw new SkipException("DataToRun for row number "+DataSet+" Is No Or Blank. So Skipping Its Execution.");
		}
		Reporter.log("<br>Screenshots of " + TestCaseName);
		loadWebBrowser(DataBrowser);		
		driver.get(DataURL);
		userLoginECC(DataUsername, DataPassword);
		Add_Log.info("Logged in");
		
		// Select Menu TRAC
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		wait.until(ExpectedConditions.elementToBeClickable(By.name("tracImage")));
		driver.findElement(By.name("tracImage")).click();
		Add_Log.info("Selected TRAC");
		
		// Select "View Statements"
		wait.until(ExpectedConditions.elementToBeClickable(By.name("DocumentViewStmtImage")));
		driver.findElement(By.name("DocumentViewStmtImage")).click();
		Add_Log.info("Selected 'View Statements'");
		
		// Enter Search Criteria and View Document
		wait.until(ExpectedConditions.presenceOfElementLocated((By.name("tax_id"))));
		driver.findElement(By.name("tax_id")).sendKeys(DataTaxID);
		
		// Enter Begin Date and End Date
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("ststartdate")));
		driver.findElement(By.name("ststartdate")).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("stenddate")));
		driver.findElement(By.name("stenddate")).clear();
		String timeStamp = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime());
		driver.findElement(By.name("ststartdate")).sendKeys(DataBeginDate);
		driver.findElement(By.name("stenddate")).sendKeys(timeStamp);
		
		// Click View document button
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
		driver.findElement(By.name("Submit")).click();
		
		wait.until(ExpectedConditions.titleContains("eCC System - Document List"));
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[1]/td/font/b")), "Click on date to View the Document:"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/table[2]")));
		// Get all search result. Verify one of the statement is presented.
		String statementList = driver.findElement(By.xpath("html/body/center/table[2]")).getText();
		if(!statementList.contains("201")) // Check statement year 201X
		{
			s_assert.assertTrue(statementList.contains("201"), "Tested statement NOT FOUND");
        	Testfail = true;
		}
		else
		{
			Add_Log.info("Statment Lists are displayed. Click on one of the statement");
		}
		
		// Click any statement e.g. the second statement
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[2]/td/font/a[2]")));
		driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[2]/td/font/a[2]")).click();
		try {
			  Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
			  Thread.currentThread().interrupt();
			}
		
		String currentURL = driver.getCurrentUrl();
		if(currentURL.contains(".onlinefinancialdocs")) // current URL on Chrome and FF are the same. Copy to clipboard does not work on this test case's PDF
		{
	    	fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatement");
			Add_Log.info("PDF can be opened.");
		}
		else
		{
				Add_Log.info("FAIL: Error occurs upon opening PDF");
				Testfail = true;
				s_assert.assertTrue(driver.getCurrentUrl().contains(".onlinefinancialdocs"), "Error occurs upon opening PDF");
				fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatementFail");
		}
		// Click browser back button and click logoff
		driver.navigate().back();
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText("Logoff"))));
		Add_Log.info("Logging off");
		driver.get(DataURL);
		// Adding screenshot to report =======================================================================================================================
		
		reportOutput(fileName1_statement, "1. View Statement", "1. Error occurs on Statement file");

		//=======================================================================================================================================================
		//driver.get(DataURL);
		
		if(Testfail){
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
	
		
	//This data provider method will return column's data one by one In every Iteration.
	@DataProvider
	public Object[][] SuiteECCCase16_CalvertData(){
		//Last two columns (DataToRun and Pass/Fail/Skip) are Ignored programatically when reading test data.
		return SuiteUtility.GetTestDataUtility(FilePath, TestCaseName);
	}	
	
	//To report result as pass or fail for test cases In TestCasesList sheet.
	@AfterTest
	public void closeBrowser(){
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
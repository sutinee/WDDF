package com.stta.SuiteECC;

import java.io.IOException;
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

public class SuiteECCCase3_AFS_T extends SuiteECCBase{
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
	
	@Test(dataProvider="SuiteECCCase3_AFS_TData")
	public void SuiteECCCase3_AFS_TDataTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword) throws InterruptedException, IOException{
		
		DataSet++;
		s_assert = new SoftAssert();
		String fileName1_emailBatch = "";

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
		
		// Select Menu Confirmations
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		wait.until(ExpectedConditions.elementToBeClickable(By.name("ConDailyImage")));
		driver.findElement(By.name("ConDailyImage")).click();
		Add_Log.info("Selected Menu Confirmations");
		
		// Select "View Emails"
		wait.until(ExpectedConditions.elementToBeClickable(By.name("DocumentEmailImage")));
		driver.findElement(By.name("DocumentEmailImage")).click();
		Add_Log.info("Selected 'View Emails'");
		
		// Wait for Consolidated Daily E-mail Batches to be loaded.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("Submit")));
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/font/b")), "Click the following links to open the batch:"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/form[4]/table/tbody/tr[2]/td[3]/a/font")));
		// Verify one of the Batch ID link is presented i.e dtc20150227170313377376-1.tar
		String BatchIDList = driver.findElement(By.xpath("html/body/center/form[4]/table/tbody/tr[2]/td[3]/a/font")).getText();
		if(!BatchIDList.contains("dtc"))
		{
			s_assert.assertTrue(BatchIDList.contains("dtc"), "Tested Batch ID NOT FOUND");
			Add_Log.info("Tested Batch ID NOT FOUND");
		    Testfail = true;
		}
		else
		{
			Add_Log.info("Tested Batch ID is FOUND");
		}
		// Click on 1st Email Batch ID link
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/form[4]/table/tbody/tr[2]/td[3]/a/font")));
		driver.findElement(By.xpath("html/body/center/form[4]/table/tbody/tr[2]/td[3]/a/font")).click();
		try {
			  Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
			  Thread.currentThread().interrupt();
			}
		
		Add_Log.info("Clicked Email Batch ID");
		
		// Verify that Email Batch ID can be opened.
		if(driver.getTitle().contains("Audit"))
		{
			Add_Log.info("Email Batch can be opened successufully");
			fileName1_emailBatch = takeScreenshot(TestCaseName, "1_View EmailBatch");
		}
		else 	
		{
			Add_Log.info("FAIL: Error occurs upon viewing Email Batch");
			fileName1_emailBatch = takeScreenshot(TestCaseName, "1_View EmailBatchFail");
			s_assert.assertTrue(driver.getTitle().contains("Audit"), "Error occurs upon viewing Email Batch");
			Testfail = true;
		}
		//driver.get(DataURL);
		// Adding screenshot to report =======================================================================================================================
		
		reportOutput(fileName1_emailBatch, "1. View Email Batch", "1. Error occurs opening Email Batch");

		//=======================================================================================================================================================

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
	public Object[][] SuiteECCCase3_AFS_TData(){
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
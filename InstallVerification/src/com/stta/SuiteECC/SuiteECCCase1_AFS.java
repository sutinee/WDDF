package com.stta.SuiteECC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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

public class SuiteECCCase1_AFS extends SuiteECCBase{
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
	
	@Test(dataProvider="SuiteECCCase1_AFSData")
	public void SuiteECCCase1_AFSDataTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword, String DataSSN, String DataBeginDate) throws InterruptedException, IOException{
		
		DataSet++;
		s_assert = new SoftAssert();
		WebElement insertLink;
		String insertURL = "";
		String fileName1_statement = "";
		String fileName2_insert = "";

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
		
		// Select Menu Statements
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		wait.until(ExpectedConditions.elementToBeClickable(By.name("image")));
		driver.findElement(By.name("image")).click();
		Add_Log.info("Selected Statement");
		
		// Select "View Statements" from the 'CEVP PDF FEATURES". Using by xpath instead of by name, because there're duplicated element name
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[2]/td/input[8]")));
		driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[2]/td/input[8]")).click();
		Add_Log.info("Selected 'View Statements' from the 'CEVP PDF FEATURES'");
		
		// Enter Search Criteria and View Document
		wait.until(ExpectedConditions.presenceOfElementLocated((By.name("sttaxid"))));
		driver.findElement(By.name("sttaxid")).sendKeys(DataSSN);
		
		// Select radial button as "Enabled Only"
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/form/table[2]/tbody/tr[2]/td/input")));
		driver.findElement(By.xpath("html/body/center/form/table[2]/tbody/tr[2]/td/input")).click();
		Add_Log.info("SSN Entered. Radial button is Enabled Only");
		
		// Enter Begin Date and End Date
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("ststartdate")));
		driver.findElement(By.name("ststartdate")).clear();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("stenddate")));
		driver.findElement(By.name("stenddate")).clear();
		String timeStamp = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime());
		driver.findElement(By.name("ststartdate")).sendKeys(DataBeginDate);
		driver.findElement(By.name("stenddate")).sendKeys(timeStamp);
		
		// Click View document button
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("enter")));
		driver.findElement(By.name("enter")).click();

		wait.until(ExpectedConditions.titleContains("eCC System - Pre-CEVP Statement List"));
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[1]/td/font/b")), "Click on date to View the Statement:"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/table[2]")));
		// Get all search result. Verify one of the statement is presented.
		String statementList = driver.findElement(By.xpath("html/body/center/table[2]")).getText();
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
		// get URL of the first insert file of 
		insertLink = driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[5]/td/a[1]"));
		insertURL = insertLink.getAttribute("href"); 
       
		
		
		// Click on one of the statements i.e June 30, 2016 Statement
		// Cannot verify Statement PDF file content, because no a href attribute available. The PDF file also cannot be Ctrl A and Ctrl C. Therefore, only verify PDF window title.
		// Chrome and FF display different title.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[4]/td[1]/a/font")));
		driver.findElement(By.xpath("html/body/center/table[2]/tbody/tr/td/table/tbody/tr[4]/td[1]/a/font")).click();
		try {
			  Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
			  Thread.currentThread().interrupt();
			}
		/*Add_Log.info("Title:" + driver.getTitle());
		String currentURL = driver.getCurrentUrl();
		Add_Log.info("Current URL:" + currentURL);
		if(driver.getTitle().contains("https://www3.onlinefinancialdocs.com/tf/FANMedia"))
		{
			try {
				  Thread.sleep(8000);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatement");
			Add_Log.info("Statement can be opened successufully");
		}
		else if(!driver.getTitle().contains("AFS Statement"))
		{
			
			Add_Log.info("FAIL: Error occurs upon viewing statement file");
			fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatementFail");
			s_assert.assertTrue(driver.getTitle().contains("https://www3.onlinefinancialdocs.com/tf/FANMedia"), "Error occurs upon viewing statement file");
			Testfail = true;
		}
		
		if(driver.getTitle().contains("AFS Statement"))
		{
			try {
				  Thread.sleep(8000);
				} catch(InterruptedException ex) {
				  Thread.currentThread().interrupt();
				}
			fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatement");
			Add_Log.info("Statement can be opened successufully");
		}
		else if(!driver.getTitle().contains("https://www3.onlinefinancialdocs.com/tf/FANMedia"))
		{
			Add_Log.info("FAIL: Error occurs upon viewing statement file");
			fileName1_statement = takeScreenshot(TestCaseName, "1_ViewStatementFail");
			s_assert.assertTrue(driver.getTitle().contains("AFS Statement"), "Error occurs upon viewing statement file");
			Testfail = true;
		}*/
		String currentURL = driver.getCurrentUrl();
		if(currentURL.contains(".onlinefinancialdocs")) // current URL on Chrome and FF are the same.
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
		driver.get(insertURL);
		currentURL = driver.getCurrentUrl();
		try {
			  Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
			  Thread.currentThread().interrupt();
			}
		Add_Log.info("Insert can be opened successufully");
		// To verify Insert "Important Information"
		if(currentURL.contains("financialtrans"))
		{
			// To verify PDF Content. can use Ctrl A and Ctrl C to clipboard
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
		    if(PDFContent.contains("fund"))
		    {
		    	fileName2_insert = takeScreenshot(TestCaseName, "2_ViewInsert"); 
				Add_Log.info("Insert PDF's content is correct. As expected");
		    }
		    else
		    {
		    	Add_Log.info("PDF content does not contain expected wording: fund");
		    	Testfail = true;
		    	s_assert.assertTrue(PDFContent.contains("fund"), "PDF content does not contain expected wording: fund");
		    	fileName2_insert = takeScreenshot(TestCaseName, "2_ViewInsertFail");
		    }*/
			String PDFContent = "";
			String expectedWording = "fund";
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
		    	fileName2_insert = takeScreenshot(TestCaseName, "2_ViewInsert");
				Add_Log.info("PDF's content is correct. As expected");
		    }
		    else
		    {

		    	Add_Log.info("PDF content does not contain expected account no:" + expectedWording);
		    	Testfail = true;
		    	s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected account no: " + expectedWording);
		    	fileName2_insert = takeScreenshot(TestCaseName, "2_ViewInsertFail");
		    }
		}
		else 	
		{
			Add_Log.info("FAIL: Error occurs upon viewing insert file");
			fileName2_insert = takeScreenshot(TestCaseName, "2_ViewInsertFail");
			s_assert.assertTrue(driver.getTitle().contains("pdf"), "Error occurs upon viewing insert file");
			Testfail = true;
		}
		
		// Adding screenshot to report =======================================================================================================================
		
		reportOutput(fileName1_statement, "1. View Statement", "1. Error occurs on Statement file");
		reportOutput(fileName2_insert, "2. View Insert", "2. Error occurs on Insert file");
		//=======================================================================================================================================================
		//driver.get(DataURL); 
		//driver.close(); not in use
	
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
	public Object[][] SuiteECCCase1_AFSData(){
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
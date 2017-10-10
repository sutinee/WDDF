package com.stta.SuiteCSR;

import java.io.IOException;
import java.util.Set;

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

//SuiteOneCaseOne Class Inherits From SuiteOneBase Class.
//So, SuiteOneCaseOne Class Is Child Class Of SuiteOneBase Class And SuiteBase Class.
public class SuiteCSRCase6_Barclay extends SuiteCSRBase{
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
		FilePath = TestCaseListExcelCSR;		
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
	@Test(dataProvider="SuiteCSRCase6_BarclayData")
	public void SuiteCSRCase6_BarclayTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword, String DataEmulateUser, String DataGroup, String DataDocType) throws InterruptedException, IOException{
		
		DataSet++;
		
		//Created object of testng SoftAssert class.
		s_assert = new SoftAssert();
		String fileName_PDF = "";
		
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
		userLoginCSR(DataUsername, DataPassword);
		if(driver.getTitle().contains("Emulate User Selection"))
		{
			emulateUser(DataEmulateUser);
		}
		
		selectDocType(DataGroup, DataDocType);
		WebDriverWait wait = new WebDriverWait(driver, timeOutSet);
		// No need to enter search criteria. Use default Begin and End date
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchButton")));
		driver.findElement(By.id("searchButton")).click();
				
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(".//*[@id='results']/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td[1]/table/tbody/tr/td[2]/span")), "SEARCH RESULTS"));
		
		// Verify search result 1st row and number of search result
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='batchId-1']/a/font")));
		WebElement firstBatch = driver.findElement(By.xpath(".//*[@id='batchId-2']/a/font"));
		// Batch ID changes every month, therefore the script do not assert BatchID
		/*if(firstBatch.getText().contains("0160"))
		{
			Add_Log.info("Tested Col 'BATCH ID' is found");
		} else {
			s_assert.assertTrue(driver.findElement(By.xpath(".//*[@id='batchId-1']/a/font")).getText().contains("0160"), "Tested Col 'BATCH ID' does not contain '0160'");
			Add_Log.info("Tested Col 'BATCH ID' does not contain '0160'");
			Testfail = true;
		}*/
		//-------------------------------------------------------------------------------------------------------------
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='pdfLink']/span/font")));
		if(driver.findElement(By.xpath(".//*[@id='pdfLink']/span/font")).getText().equals("View PDF"))
		{
			Add_Log.info("Tested Col 'AUDIT REPORT' is correct: 'View PDF'");
		} else {
			s_assert.assertEquals(driver.findElement(By.xpath(".//*[@id='pdfLink']/span/font")).getText(), "View PDF", "Test Result Col 'AUDIT REPORT' is not as expected");
			Add_Log.info("FAIL: Test Result Col 'AUDIT REPORT' is not as expected");
			Testfail = true;
		}
		//-------------------------------------------------------------------------------------------------------------
		/*wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='theBody']/tr[1]/td[7]/div")));
		if(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[7]/div")).getText().contains("CSRWEB"))
		{
			Add_Log.info("Tested Col 'LOAD TYPE' is correct, containing 'CSRWEB'");
		} else {
			s_assert.assertTrue(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[7]/div")).getText().contains("CSRWEB"), "Tested Col 'LOAD TYPE' does not contain 'CSRWEB'");
			Add_Log.info("Tested Col 'LOAD TYPE' does not contain 'CSRWEB'");
			Testfail = true;
		}*/

		//Click on Batch ID to open "Sample" dialog box
		// get BatchID
		String BatchID = firstBatch.getText();
		firstBatch.click();
		Add_Log.info("Clicked BATCH ID");
		
		//Sample dialog box
		wait.until(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath(".//*[@id='documentsForBatchIdDiv']/div")), BatchID));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='documentsForBatchIdDiv']/div/table/tbody/tr[3]/td/div/ul/li[1]/a")));
		driver.findElement(By.xpath(".//*[@id='documentsForBatchIdDiv']/div/table/tbody/tr[3]/td/div/ul/li[1]/a")).click();
		Add_Log.info("Clicked Sample Document");
		
		// Verify PDF can be opened.
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("next")));
		Set<String> AllWindowHandles = driver.getWindowHandles();
		String window1 = (String) AllWindowHandles.toArray()[0]; // Main CSR window
		String window2 = (String) AllWindowHandles.toArray()[1]; // PDF window
						
		driver.switchTo().window(window2);
		driver.manage().window().maximize();
		try {
				Thread.sleep(waitFileLoad);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		String currentURL = driver.getCurrentUrl();
		if(currentURL.contains(".onlinefinancialdocs")) // current URL on Chrome and FF are the same.
		{
				String PDFContent = "";
				String expectedWording = "card";
				PDFContent = verifyPDFContent();
				// Incase PDF is taking long time to load. Wait x2 times and get clip board again
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
				    fileName_PDF = takeScreenshot(TestCaseName, "1_ViewPDF");
					Add_Log.info("PDF's content is correct. As expected");
			    }
			    else
			    {

			    	Add_Log.info("PDF content does not contain expected account no:" + expectedWording);
			    	Testfail = true;
			    	s_assert.assertTrue(PDFContent.contains(expectedWording), "PDF content does not contain expected account no: " + expectedWording);
			    	fileName_PDF = takeScreenshot(TestCaseName, "1_ViewPDFFail");
			    }
		}
		else
		{
				Add_Log.info("FAIL: Error occurs upon opening PDF");
				Testfail = true;
				s_assert.assertTrue(driver.getCurrentUrl().contains(".onlinefinancialdocs"), "Error occurs upon opening PDF");
				fileName_PDF = takeScreenshot(TestCaseName, "1_ViewPDFFail");
		}
		driver.get(DataURL);
		driver.close(); // Close PDF window
		driver.switchTo().window(window1);
		
		// Adding screenshot to report =======================================================================================================================

			reportOutput(fileName_PDF, "1. View PDF file", "1. Error occurs upon opening PDF");
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
	
	@DataProvider
	public Object[][] SuiteCSRCase6_BarclayData(){
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
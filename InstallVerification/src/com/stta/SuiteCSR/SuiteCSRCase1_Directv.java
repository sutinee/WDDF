package com.stta.SuiteCSR;



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
public class SuiteCSRCase1_Directv extends SuiteCSRBase{
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
	@Test(dataProvider="SuiteCSRCase1_DirectvData")
	public void SuiteCSRCase1_DirectvTest(String DataURL, String DataBrowser, String DataUsername, String DataPassword, String DataEmulateUser, String DataGroup, String DataDocType, String DataAccountNumber, String DataBeginDate) throws InterruptedException, IOException, UnsupportedFlavorException{
		
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
		// Enter Search Criteria and click Search
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accountNumber")));
		driver.findElement(By.id("accountNumber")).sendKeys(DataAccountNumber);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("beginDate")));
		driver.findElement(By.id("beginDate")).clear();
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("endDate")));
		driver.findElement(By.id("endDate")).clear();
		String timeStamp = new SimpleDateFormat("MM-dd-YYYY").format(Calendar.getInstance().getTime());
		driver.findElement(By.id("beginDate")).sendKeys(DataBeginDate);
		driver.findElement(By.id("endDate")).sendKeys(timeStamp);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchButton")));
		driver.findElement(By.id("searchButton")).click();
		
		// Verify search result 1st row and number of search result		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("pdfLink")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='theBody']/tr[1]/td[4]/div")));
		if(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[4]/div")).getText().contains("078"))
		{
			Add_Log.info("Tested Col 'CORP#' is correct, containing '078'");
		} else {
			s_assert.assertTrue(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[4]/div")).getText().contains("078"), "Test Result Col 'CORP#' does not contain '078'");
			Add_Log.info("FAIL: Test Result Col 'CORP#' does not contain '078'");
			Testfail = true;
		}
		//-------------------------------------------------------------------------------------------------------------
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='theBody']/tr[1]/td[5]/div")));
		if(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[5]/div")).getText().equals("00033102"))
		{
			Add_Log.info("Tested Col 'ACCOUNT#' is correct: '00033102'");
		} else {
			s_assert.assertEquals(driver.findElement(By.xpath(".//*[@id='theBody']/tr[1]/td[5]/div")).getText(), "00033102", "Test Result Col 'ACCOUNT#' is not as expected");
			Add_Log.info("FAIL: Test Result Col '' is not as expected");
			Testfail = true;
		}
				
		// Verify PDF can be opened.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("pdfLink")));
		driver.findElement(By.id("pdfLink")).click();
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
		
		/*
		// Verify that the same statement list is produced in every CSR Web install ========================================================
		//Get number of rows In table.
		int Row_count = driver.findElements(By.xpath(".//*[@id='theTable']/table/tbody/tr")).size();
		int Row_count_original = Row_count;
		//Get number of columns In table.
		int Col_count = driver.findElements(By.xpath(".//*[@id='theTable']/table/tbody/tr[1]/td")).size();
		//divided xpath In three parts to pass Row_count and Col_count values.
		String first_part = "//*[@id='theTable']/table/tbody/tr[";
		String second_part = "]/td[";
		String third_part = "]";
		String tableActualResult = "";
		//String passFail = "P";
	
		//Extracts Expected Result into Array
		ExpectedResult = ExpectedResult.trim();
		String strExpectedResult[] = ExpectedResult.split("]");
		ArrayList<String> strExpectedResultList = new ArrayList<String>(Arrays.asList(strExpectedResult));
		int Row_count_expected = strExpectedResult.length;
		ArrayList<String> testResultRowsList = new ArrayList<String>();
		ArrayList<String> testActualRowsList = new ArrayList<String>();
		
	
		//System.out.println("Expected number of rows: " + Row_count_expected + "\n");
		//System.out.println("Actual number of rows: " + Row_count + "");
		
		if(Row_count_expected > Row_count)
		{
			// Adjust row of actual result
			Row_count = Row_count_expected;
			
		}
		if(Row_count_expected < Row_count)
		{
			// Adjust row of actual result
			for (int i=Row_count_expected; i<Row_count; i++)
			{
				System.out.println("i =" + i);
				strExpectedResultList.add(i, "Empty");
			}
			
		}

		for (int i=1; i<=Row_count; i++){
			System.out.println("i: " + i + "\n");
			if(i > Row_count_original)
			{
				tableActualResult = "N/A";
				testResultRowsList.add("RowFail");
				Testfail=true;
				s_assert.assertEquals(tableActualResult, strExpectedResultList.get(i-1).trim(), tableActualResult+"] And "+strExpectedResultList.get(i-1).trim()+"] Not Match");
				Add_Log.info("FAIL: Actual search result(s) are not equal to expected.");
				System.out.println("Actual Result: " + tableActualResult + "\n");
				testActualRowsList.add(tableActualResult);
			}
			else
			{
			tableActualResult = "["; 
			//Used for loop for number of columns.
			for(int j=2; j<=Col_count; j++){
				//Prepared final xpath of specific cell as per values of i and j.
				String final_xpath = first_part+i+second_part+j+third_part;
				//Will retrieve value from located cell and print It.
				String Table_data = driver.findElement(By.xpath(final_xpath)).getText();
				if (Table_data.trim().equals("")){
					Table_data = "<blank>";
				}
				
				tableActualResult = tableActualResult + Table_data;
				if (j!=Col_count){
					tableActualResult = tableActualResult + ", ";
					}else{
						// Verify test result here
						if(!(strExpectedResultList.get(i-1).trim().equals(tableActualResult))){
							System.out.println("Expected Result: " + strExpectedResultList.get(i-1).trim());
							testResultRowsList.add("RowFail");
							Testfail=true;
							s_assert.assertEquals(tableActualResult, strExpectedResultList.get(i-1).trim(), tableActualResult+"] And "+strExpectedResultList.get(i-1).trim()+"] Not Match");
							Add_Log.info("FAIL: Actual search result(s) are not equal to expected.");
							}
						else
							{
							testResultRowsList.add("RowPass");
							}
						// End Verification
						tableActualResult = tableActualResult + "]";
						System.out.println("Actual Result: " + tableActualResult + "\n");
						testActualRowsList.add(tableActualResult);
				   		}
			} // End for Column
			}
		} // End for Row

		String[] testActualRows = new String[testActualRowsList.size()];
		testActualRows = testActualRowsList.toArray(testActualRows);
		
		String[] testResultRows = new String[testResultRowsList.size()];
		testResultRows = testResultRowsList.toArray(testResultRows);
		
		SuiteUtility.WriteResultUtilityRow(FilePath, TestCaseName, "Actual Result", DataSet+1, testActualRows, testResultRows);
		//========================================================================================================================================================================
		 */
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
	public Object[][] SuiteCSRCase1_DirectvData(){
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
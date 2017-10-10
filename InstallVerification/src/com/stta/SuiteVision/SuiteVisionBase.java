package com.stta.SuiteVision;


import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import com.stta.TestSuiteBase.SuiteBase;
import com.stta.utility.Read_XLS;
import com.stta.utility.SuiteUtility;

public class SuiteVisionBase extends SuiteBase{
	
	Read_XLS FilePath = null;
	String SheetName = null;
	String SuiteName = null;
	String ToRunColumnName = null;	
	
	//This function will be executed before SuiteOne's test cases to check SuiteToRun flag.
	@BeforeSuite
	public void checkSuiteToRun() throws IOException{		
		//Called init() function from SuiteBase class to Initialize .xls Files
		init();			
		//To set TestSuiteList.xls file's path In FilePath Variable.
		FilePath = TestSuiteListExcel;
		SheetName = "SuitesList";
		SuiteName = "SuiteVision";
		ToRunColumnName = "SuiteToRun";
		
		//Bellow given syntax will Insert log In applog.log file.
		Add_Log.info("Execution started for SuitCSRBase.");
		
		//If SuiteToRun !== "y" then SuiteOne will be skipped from execution.
		if(!SuiteUtility.checkToRunUtility(FilePath, SheetName,ToRunColumnName,SuiteName)){			
			Add_Log.info("SuiteToRun = N for "+SuiteName+" So Skipping Execution.");
			//To report SuiteOne as 'Skipped' In SuitesList sheet of TestSuiteList.xls If SuiteToRun = N.
			SuiteUtility.WriteResultUtility(FilePath, SheetName, "Skipped/Executed", SuiteName, "Skipped");
			//It will throw SkipException to skip test suite's execution and suite will be marked as skipped In testng report.
			throw new SkipException(SuiteName+"'s SuiteToRun Flag Is 'N' Or Blank. So Skipping Execution Of "+SuiteName);
		}
		//To report SuiteOne as 'Executed' In SuitesList sheet of TestSuiteList.xls If SuiteToRun = Y.
		SuiteUtility.WriteResultUtility(FilePath, SheetName, "Skipped/Executed", SuiteName, "Executed");		
	}
	
	public void userLoginVision(String Username, String Password)
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("oper")));
		driver.findElement(By.name("oper")).sendKeys(Username);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.name("pswd")));
		driver.findElement(By.name("pswd")).sendKeys(Password);
		
		driver.findElement(By.name("pswd")).sendKeys(Keys.RETURN);
		
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='centeredcontent']/table/tbody/tr[1]/td[1]/table/tbody/tr[5]/td/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[5]/td[3]/input")));
		//driver.findElement(By.xpath(".//*[@id='centeredcontent']/table/tbody/tr[1]/td[1]/table/tbody/tr[5]/td/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr[5]/td[3]/input")).click();
	}
	
	public void accessSSNPage(String firstChar, String mgmtCo, String DataSSN) throws InterruptedException
	{
		//Click 'I agree' button on Vision Disclaimer page
		
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.titleContains("Vision - Disclaimer"));
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='agree']/input[9]")));
		driver.findElement(By.xpath(".//*[@id='agree']/input[9]")).click();
		//Select specific character from 'View' on the 'Asset Summary section'
		wait.until(ExpectedConditions.titleContains("Vision - Operations"));
		Thread.sleep(500);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText(firstChar)));
		WebElement linkA = driver.findElement(By.linkText(firstChar));
		linkA.click();
		Add_Log.info("Selected '"+firstChar+"' from 'View' on the 'Asset Summary section'");
		driver.switchTo().defaultContent(); // you are now outside both frames
		driver.switchTo().frame("mfGbobFrameInOut");
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='trLineItem']/td[1]/input[2]")));
		WebElement firstList = driver.findElement(By.xpath(".//*[@id='trLineItem']/td[1]/input[2]"));
		firstList.click();
		
		// Change mgmt co 
		Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("mid")));
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='frmManagement']/table/tbody/tr/td[2]/input")));
		Select dropdown = new Select(driver.findElement(By.id("mid")));
		dropdown.selectByVisibleText(mgmtCo);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@id='frmManagement']/table/tbody/tr/td[2]/input")));
		driver.findElement(By.xpath(".//*[@id='frmManagement']/table/tbody/tr/td[2]/input")).click();
		wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("html/body/table[1]/tbody/tr[2]/td/table[2]/tbody/tr[1]/td[2]/table/tbody/tr[2]/td"), mgmtCo));
		Add_Log.info("Selected" + mgmtCo);
				

		// Click the 'Tax ID/SSN' link on the left navigation bar
		
		wait.until(ExpectedConditions.titleContains("Vision - Assets"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Tax ID/SSN")));
		driver.findElement(By.linkText("Tax ID/SSN")).click();
		wait.until(ExpectedConditions.titleContains("Vision - Tax ID / SSN Search"));
		
		//Enter SSN
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchText")));
		driver.findElement(By.id("searchText")).sendKeys(DataSSN);
		Add_Log.info("Entered SSN");
		// Click 'Search' button
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchButton")));
		driver.findElement(By.id("searchButton")).click();
		Add_Log.info("Clicked Search");
	}
	
	public boolean checkAccount(String accountNo)
	// Scroll down the page till you see Account #
	{
		 //To locate table.
		  WebDriverWait wait = new WebDriverWait(driver, 60);
		  wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr/td/table")));
		  WebElement mytable = driver.findElement(By.xpath("html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr/td/table"));
		  //To locate rows of table.
		  List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
		  //To calculate no of rows In table.
		  int rows_count = rows_table.size();
		  boolean accountExist = false;
		  
		  //Loop will execute till the last row of table.
		  for (int row=0; row<rows_count; row++){
		   //To locate columns(cells) of that specific row.
		   List<WebElement> Columns_row = rows_table.get(row).findElements(By.tagName("td"));
		   //To calculate no of columns(cells) In that specific row.
		   //int columns_count = Columns_row.size();
		   int columns_count = 1;
				   
		   //Loop will execute till the last cell of that specific row.
		   		for (int column=0; column<columns_count; column++){
		   			//To retrieve text from that specific cell.
		   			String celtext = Columns_row.get(column).getText();
		   			if(celtext.contains(accountNo))
		   			{
		   				accountExist = true;
		   			}
		   		}// end 1st for
		  }  // end 2nd for
		  
		  return accountExist;
	}
	
	public void searchAccount(String accountNo)
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Account Number")));
		driver.findElement(By.linkText("Account Number")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("acctnum")));
		driver.findElement(By.id("acctnum")).sendKeys(accountNo); 
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("searchButton")));
		driver.findElement(By.id("searchButton")).click();
		
		// Click 'Go' button next to Acct Details
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu")));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/div[1]/table[2]/tbody/tr/td[2]/table[6]/tbody/tr/td/table/tbody/tr[7]/td[1]/input[1]")));
		driver.findElement(By.xpath("html/body/div[1]/table[2]/tbody/tr/td[2]/table[6]/tbody/tr/td/table/tbody/tr[7]/td[1]/input[1]")).click();
		Add_Log.info("Clicked 'Go' button next to Acct Details");
	}
	



}

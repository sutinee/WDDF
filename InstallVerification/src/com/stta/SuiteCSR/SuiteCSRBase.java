package com.stta.SuiteCSR;


import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;
import com.stta.TestSuiteBase.SuiteBase;
import com.stta.utility.Read_XLS;
import com.stta.utility.SuiteUtility;

public class SuiteCSRBase extends SuiteBase{
	
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
		SuiteName = "SuiteCSR";
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
	
	public void userLoginCSR(String Username, String Password)
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("userName")));
		driver.findElement(By.id("userName")).sendKeys(Username);
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
		driver.findElement(By.id("password")).sendKeys(Password); 
		
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("submit")));
		driver.findElement(By.id("submit")).click();
	}
	
	public void emulateUser(String EmulateUser)
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		
		Add_Log.info("Logged in successfully");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(EmulateUser)));
		driver.findElement(By.linkText(EmulateUser)).click();
		
	}
	
	public void selectDocType(String DataGroup, String DataDocType)
	{
		Add_Log.info("Emulated User successfully");
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='doctypeSelect']/option[1]")));
		Select listboxGroup = new Select(driver.findElement(By.id("doctypeSelect")));
		listboxGroup.selectByVisibleText(DataGroup);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='subDoctypeSelect']/option[1]")));
		Select listboxDocType = new Select(driver.findElement(By.id("subDoctypeSelect")));
		listboxDocType.selectByVisibleText(DataDocType);
		Add_Log.info("Selected doctype successfully");
	}
	

}
